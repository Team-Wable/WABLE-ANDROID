package com.teamwable.profile.profile.edit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.util.getThrowableMessage
import com.teamwable.designsystem.component.snackbar.SNACK_BAR_DURATION
import com.teamwable.designsystem.component.snackbar.WableSnackBar
import com.teamwable.designsystem.component.snackbar.WableSnackBarPopUp
import com.teamwable.designsystem.type.ProfileEditType
import com.teamwable.designsystem.type.ProfileImageType
import com.teamwable.designsystem.type.SnackBarType
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.onboarding.profile.ProfileScreen
import com.teamwable.onboarding.profile.model.ProfileSideEffect
import com.teamwable.onboarding.profile.permission.launchImagePicker
import com.teamwable.onboarding.profile.permission.rememberGalleryLauncher
import com.teamwable.onboarding.profile.permission.rememberPhotoPickerLauncher
import com.teamwable.profile.profile.edit.model.ProfilePatchState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ProfileEditRoute(
    viewModel: ProfileEditViewModel = hiltViewModel(),
    profile: MemberInfoEditModel,
    navigateToProfile: (MemberInfoEditModel) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val loadingState by viewModel.profileLoadingState.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val onShowErrorSnackBar: (throwable: Throwable?) -> Unit = { throwable ->
        coroutineScope.launch {
            val job = launch {
                snackBarHostState.showSnackbar(message = throwable.getThrowableMessage())
            }
            delay(SNACK_BAR_DURATION)
            job.cancel()
        }
    }

    val galleryLauncher = rememberGalleryLauncher { uri ->
        viewModel.onImageSelected(uri.toString())
    }

    val photoPickerLauncher = rememberPhotoPickerLauncher { uri ->
        viewModel.onImageSelected(uri.toString())
    }

    LaunchedEffect(Unit) {
        viewModel.onNicknameChanged(profile.nickname.orEmpty())
        val profileType = ProfileImageType.entries.find { it.name == profile.memberDefaultProfileImage }
        if (profileType != null) {
            viewModel.onRandomImageChange(profileType)
        } else {
            viewModel.onImageSelected(profile.memberDefaultProfileImage)
        }
    }

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ProfileSideEffect.NavigateToProfile -> navigateToProfile(
                        profile.copy(
                            nickname = profileState.nickname,
                            memberDefaultProfileImage = profileState.selectedImageUri,
                        ),
                    )

                    is ProfileSideEffect.RequestImagePicker -> context.launchImagePicker(galleryLauncher, photoPickerLauncher)
                    is ProfileSideEffect.ShowSnackBar -> onShowErrorSnackBar(sideEffect.message)

                    else -> Unit
                }
            }
    }

    ProfileScreen(
        profileState = profileState,
        profileEditType = ProfileEditType.PROFILE,
        onNextBtnClick = { nickname, imageUri, defaultImage ->
            viewModel.patchUserProfile(
                memberInfoEditModel = profile.copy(
                    nickname = nickname,
                    memberDefaultProfileImage = defaultImage,
                ),
                imgUrl = imageUri,
            )
            viewModel.onImageSelected(imageUri ?: defaultImage)
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

    SnackbarHost(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        hostState = snackBarHostState,
        snackbar = { snackBarData ->
            WableSnackBar(
                message = snackBarData.visuals.message,
                snackBarType = SnackBarType.ERROR,
            )
        },
    )

    WableSnackBarPopUp(
        isVisible = loadingState is ProfilePatchState.Loading,
        snackBarType = SnackBarType.LOADING_PROFILE,
    )
}
