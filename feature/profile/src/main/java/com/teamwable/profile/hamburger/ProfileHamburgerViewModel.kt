package com.teamwable.profile.hamburger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.common.uistate.UiState
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.profile.MemberDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
}
