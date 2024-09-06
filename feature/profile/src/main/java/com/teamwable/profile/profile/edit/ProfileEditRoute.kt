package com.teamwable.profile.profile.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.onboarding.profile.ProfileScreen
import com.teamwable.onboarding.profile.model.ProfileSideEffect
import com.teamwable.onboarding.profile.permission.launchImagePicker
import com.teamwable.onboarding.profile.permission.rememberGalleryLauncher
import com.teamwable.onboarding.profile.permission.rememberPhotoPickerLauncher

@Composable
fun ProfileEditRoute(
    viewModel: ProfileEditViewModel = hiltViewModel(),
    navigateToProfile: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    val galleryLauncher = rememberGalleryLauncher { uri ->
        viewModel.onImageSelected(uri.toString())
    }

    val photoPickerLauncher = rememberPhotoPickerLauncher { uri ->
        viewModel.onImageSelected(uri.toString())
    }

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ProfileSideEffect.NavigateToProfile -> navigateToProfile()
                    is ProfileSideEffect.RequestImagePicker -> context.launchImagePicker(galleryLauncher, photoPickerLauncher)

                    else -> Unit
                }
            }
    }

    ProfileScreen(
        profileState = profileState,
        onNextBtnClick = { nickname, imageUri, defaultImage ->
            viewModel.navigateToProfile()
        },
        onProfilePlusBtnClick = { viewModel.requestImagePicker() },
        onDuplicateBtnClick = { viewModel.getNickNameValidation() },
        onRandomImageChange = { newImage ->
            viewModel.onRandomImageChange(newImage)
            viewModel.onImageSelected(null)
        },
        onNicknameChange = { newNickname ->
            viewModel.onNicknameChanged(newNickname)
        },
    )
}
