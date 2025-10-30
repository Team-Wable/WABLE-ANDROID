package com.teamwable.profile.hamburger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.model.profile.MemberInfoEditModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileHamburgerViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _memberDataUiState = MutableStateFlow<UiState<MemberDataModel>>(UiState.Empty)
    val memberDataUiState = _memberDataUiState.asStateFlow()

    private val _withdrawalUiState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val withdrawalUiState = _withdrawalUiState.asStateFlow()

    private var _pushAlarmAllowedState = MutableStateFlow(false)
    val pushAlarmAllowedState = _pushAlarmAllowedState.asStateFlow()

    fun getMemberData() {
        viewModelScope.launch {
            _memberDataUiState.value = UiState.Loading
            profileRepository.getMemberData()
                .onSuccess { _memberDataUiState.value = UiState.Success(it) }
                .onFailure { _memberDataUiState.value = UiState.Failure(it.message.toString()) }
        }
    }

    fun patchCheck(deletedReason: List<String>) =
        viewModelScope.launch {
            profileRepository.patchWithdrawal(deletedReason)
                .onSuccess { _withdrawalUiState.value = UiState.Success(it) }
                .onFailure { _withdrawalUiState.value = UiState.Failure(it.message.toString()) }
        }

    fun clearInfo() {
        viewModelScope.launch {
            userInfoRepository.clearAll()
        }
    }

    fun saveIsAutoLogin(check: Boolean) {
        viewModelScope.launch {
            userInfoRepository.saveAutoLogin(check)
        }
    }

    fun patchUserProfileUri(info: MemberInfoEditModel, url: String? = null) {
        viewModelScope.launch {
            profileRepository.patchUserProfile(info, url)
                .onSuccess { info.isPushAlarmAllowed?.let { _pushAlarmAllowedState.value = it } }
                .onFailure { Timber.d("fail", it.message.toString()) }
        }
    }

    fun saveIsPushAlarmAllowed(isPushAlarmAllowed: Boolean) =
        viewModelScope.launch {
            userInfoRepository.saveIsPushAlarmAllowed(isPushAlarmAllowed)
        }
}
