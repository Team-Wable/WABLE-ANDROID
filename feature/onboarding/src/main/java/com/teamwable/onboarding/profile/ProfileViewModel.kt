package com.teamwable.onboarding.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teamwable.common.base.BaseViewModel
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.designsystem.type.NicknameType
import com.teamwable.designsystem.type.ProfileImageType
import com.teamwable.model.network.Error
import com.teamwable.navigation.Route
import com.teamwable.onboarding.profile.model.ProfileIntent
import com.teamwable.onboarding.profile.model.ProfileSideEffect
import com.teamwable.onboarding.profile.model.ProfileState
import com.teamwable.onboarding.profile.naviagation.ProfileTypeMap
import com.teamwable.onboarding.profile.regex.NicknameValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val nicknameValidationUseCase: NicknameValidationUseCase,
    private val profileRepository: ProfileRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ProfileIntent, ProfileState, ProfileSideEffect>(
        initialState = ProfileState(),
    ) {
    private val args = savedStateHandle.toRoute<Route.Profile>(
        typeMap = ProfileTypeMap.typeMap,
    )

    override fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.UpdatePhotoPermission -> updatePhotoPermissionState(intent.isGranted)
            is ProfileIntent.OnImageSelected -> selectImage(intent.imageUri)
            is ProfileIntent.OnNicknameChanged -> onNicknameChanged(intent.newNickname)
            is ProfileIntent.GetNickNameValidation -> getNickNameValidation()
            is ProfileIntent.OnRandomImageChange -> onRandomImageChange(intent.newImage)
            is ProfileIntent.OpenDialog -> openDialog(intent.isOpened)
            is ProfileIntent.RequestImagePicker -> requestImagePicker()
            is ProfileIntent.OnNextBtnClick -> navigateToAgreeTerms(intent.nickName, intent.defaultImage)
        }
    }

    private fun updatePhotoPermissionState(isGranted: Boolean) {
        intent { copy(isPermissionGranted = isGranted) }
    }

    private fun selectImage(imageUri: String?) {
        intent { copy(selectedImageUri = imageUri) }
    }

    private fun onNicknameChanged(newNickname: String) {
        intent {
            copy(
                nickname = newNickname,
                textFieldType = nicknameValidationUseCase(newNickname),
            )
        }
    }

    private fun getNickNameValidation() {
        viewModelScope.launch {
            profileRepository.getNickNameDoubleCheck(currentState.nickname)
                .onSuccess {
                    intent { copy(textFieldType = NicknameType.CORRECT) }
                }
                .onFailure {
                    when (it) {
                        is Error.ApiError -> intent { copy(textFieldType = NicknameType.DUPLICATE) }
                        else -> postSideEffect(ProfileSideEffect.ShowSnackBar(it))
                    }
                }
        }
    }

    private fun onRandomImageChange(newImage: ProfileImageType) {
        intent { copy(currentImage = newImage) }
    }

    private fun openDialog(isOpened: Boolean) {
        intent { copy(openDialog = isOpened) }
    }

    private fun navigateToAgreeTerms(nickName: String, defaultImage: String?) {
        postSideEffect(
            ProfileSideEffect.NavigateToAgreeTerms(
                args.memberInfoEditModel.copy(
                    nickname = nickName,
                    memberDefaultProfileImage = defaultImage,
                ),
            ),
        )
    }

    private fun requestImagePicker() {
        if (currentState.isPermissionGranted) postSideEffect(ProfileSideEffect.RequestImagePicker)
        else intent { copy(openDialog = true) }
    }
}
