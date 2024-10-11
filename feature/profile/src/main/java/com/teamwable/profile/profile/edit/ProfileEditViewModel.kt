package com.teamwable.profile.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.designsystem.type.NicknameType
import com.teamwable.designsystem.type.ProfileImageType
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.onboarding.profile.model.ProfileSideEffect
import com.teamwable.onboarding.profile.model.ProfileState
import com.teamwable.onboarding.profile.regex.NicknameValidationUseCase
import com.teamwable.profile.profile.edit.model.ProfilePatchState
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
internal class ProfileEditViewModel @Inject constructor(
    private val nicknameValidationUseCase: NicknameValidationUseCase,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect: SharedFlow<ProfileSideEffect> = _sideEffect.asSharedFlow()

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState

    private val _profilePatchState = MutableStateFlow<ProfilePatchState>(ProfilePatchState.Idle)
    val profileLoadingState: StateFlow<ProfilePatchState> get() = _profilePatchState

    fun requestImagePicker() {
        viewModelScope.launch {
            _sideEffect.emit(ProfileSideEffect.RequestImagePicker)
        }
    }

    fun onImageSelected(imageUri: String?) {
        viewModelScope.launch {
            _profileState.update { it.copy(selectedImageUri = imageUri) }
        }
    }

    fun onNicknameChanged(newNickname: String) {
        _profileState.update { it.copy(nickname = newNickname) }
        validateNickname(newNickname)
    }

    fun onRandomImageChange(newImage: ProfileImageType) {
        _profileState.update { it.copy(currentImage = newImage) }
    }

    private fun validateNickname(nickname: String) {
        viewModelScope.launch {
            _profileState.update { it.copy(textFieldType = nicknameValidationUseCase(nickname)) }
        }
    }

    fun getNickNameValidation() {
        viewModelScope.launch {
            profileRepository.getNickNameDoubleCheck(_profileState.value.nickname)
                .onSuccess {
                    _profileState.update { it.copy(textFieldType = NicknameType.CORRECT) }
                }
                .onFailure {
                    _profileState.update { it.copy(textFieldType = NicknameType.DUPLICATE) }
                }
        }
    }

    fun patchUserProfile(memberInfoEditModel: MemberInfoEditModel, imgUrl: String?) {
        viewModelScope.launch {
            _profilePatchState.update { ProfilePatchState.Loading }
            profileRepository.patchUserProfile(memberInfoEditModel, imgUrl)
                .onSuccess {
                    _profilePatchState.update { ProfilePatchState.Idle }
                    _sideEffect.emit(ProfileSideEffect.NavigateToProfile)
                }
                .onFailure {
                    _profilePatchState.update { ProfilePatchState.Idle }
                    _sideEffect.emit(ProfileSideEffect.ShowSnackBar(it))
                }
        }
    }
}
