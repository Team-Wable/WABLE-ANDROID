package com.teamwable.onboarding.agreeterms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.onboarding.agreeterms.model.AgreeTermsSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgreeTermsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<AgreeTermsSideEffect>()
    val sideEffect: SharedFlow<AgreeTermsSideEffect> = _sideEffect.asSharedFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog

    fun navigateToHome() {
        viewModelScope.launch {
            _sideEffect.emit(AgreeTermsSideEffect.NavigateToHome)
        }
    }

    fun showLoginDialog(show: Boolean) {
        _showDialog.update { show }
    }

    fun patchUserProfile(memberInfoEditModel: MemberInfoEditModel, imgUrl: String?) {
        viewModelScope.launch {
            profileRepository.patchUserProfile(memberInfoEditModel, imgUrl)
                .onSuccess {
                    _sideEffect.emit(AgreeTermsSideEffect.ShowDialog)
                    userInfoRepository.saveAutoLogin(true)
                }
                .onFailure {
                    _sideEffect.emit(AgreeTermsSideEffect.ShowSnackBar(it))
                }
        }
    }
}
