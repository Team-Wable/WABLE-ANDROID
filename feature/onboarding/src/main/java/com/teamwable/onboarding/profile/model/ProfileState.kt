package com.teamwable.onboarding.profile.model

import androidx.compose.runtime.Immutable
import com.teamwable.designsystem.type.NicknameType
import com.teamwable.designsystem.type.ProfileImageType

@Immutable
data class ProfileState(
    val nickname: String = "",
    val textFieldType: NicknameType = NicknameType.DEFAULT,
    val selectedImageUri: String? = null,
    val currentImage: ProfileImageType = ProfileImageType.entries.random(),
    val isPermissionGranted: Boolean = false,
    val openDialog: Boolean = false,
)
