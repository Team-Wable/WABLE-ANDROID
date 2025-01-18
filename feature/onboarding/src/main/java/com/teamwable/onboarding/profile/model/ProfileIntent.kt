package com.teamwable.onboarding.profile.model

import com.teamwable.common.base.BaseIntent
import com.teamwable.designsystem.type.ProfileImageType

sealed interface ProfileIntent : BaseIntent {
    data class UpdatePhotoPermission(val isGranted: Boolean) : ProfileIntent

    data class OnImageSelected(val imageUri: String?) : ProfileIntent

    data class OnNicknameChanged(val newNickname: String) : ProfileIntent

    data object GetNickNameValidation : ProfileIntent

    data class OnRandomImageChange(val newImage: ProfileImageType) : ProfileIntent
}
