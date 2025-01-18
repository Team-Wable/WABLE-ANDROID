package com.teamwable.onboarding.profile

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.util.AmplitudeSignUpTag.CLICK_ADD_PICTURE_PROFILE_SIGNUP
import com.teamwable.common.util.AmplitudeSignUpTag.CLICK_CHANGE_PICTURE_PROFILE_SIGNUP
import com.teamwable.common.util.AmplitudeSignUpTag.CLICK_NEXT_PROFILE_SIGNUP
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.button.WableSmallButton
import com.teamwable.designsystem.component.dialog.PermissionAppSettingsDialog
import com.teamwable.designsystem.component.textfield.WableBasicTextField
import com.teamwable.designsystem.extension.system.navigateToAppSettings
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.NicknameType
import com.teamwable.designsystem.type.ProfileEditType
import com.teamwable.designsystem.type.ProfileImageType
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.navigation.Route
import com.teamwable.onboarding.R
import com.teamwable.onboarding.profile.component.ProfileImagePicker
import com.teamwable.onboarding.profile.model.ProfileIntent
import com.teamwable.onboarding.profile.model.ProfileSideEffect
import com.teamwable.onboarding.profile.model.ProfileState
import com.teamwable.onboarding.profile.permission.launchImagePicker
import com.teamwable.onboarding.profile.permission.rememberGalleryLauncher
import com.teamwable.onboarding.profile.permission.rememberPhotoPickerLauncher
import timber.log.Timber

@Composable
internal fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    args: Route.Profile,
    navigateToAgreeTerms: (MemberInfoEditModel, String?) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val profileState by viewModel.uiState.collectAsStateWithLifecycle()

    var memberInfoEditModel by remember { mutableStateOf(args.memberInfoEditModel) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        try {
            if (isGranted) viewModel.onIntent(ProfileIntent.UpdatePhotoPermission(true))
            else viewModel.onIntent(ProfileIntent.OpenDialog(true))
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    LaunchedEffect(Unit) { // todo : 생명주기 확인 요망
        val permission = when {
            Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU -> Manifest.permission.READ_MEDIA_IMAGES
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> Manifest.permission.READ_EXTERNAL_STORAGE
            else -> return@LaunchedEffect
        }

        permissionLauncher.launch(permission)
    }

    val galleryLauncher = rememberGalleryLauncher { uri ->
        viewModel.onIntent(ProfileIntent.OnImageSelected(uri.toString()))
    }

    val photoPickerLauncher = rememberPhotoPickerLauncher { uri ->
        viewModel.onIntent(ProfileIntent.OnImageSelected(uri.toString()))
    }

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is ProfileSideEffect.NavigateToAgreeTerms -> navigateToAgreeTerms(memberInfoEditModel, profileState.selectedImageUri)

                    is ProfileSideEffect.ShowPermissionDeniedDialog -> viewModel.onIntent(ProfileIntent.OpenDialog(true))

                    is ProfileSideEffect.RequestImagePicker -> context.launchImagePicker(galleryLauncher, photoPickerLauncher)

                    is ProfileSideEffect.ShowSnackBar -> onShowErrorSnackBar(sideEffect.message)

                    else -> Unit
                }
            }
    }

    if (profileState.openDialog) {
        PermissionAppSettingsDialog(
            onClick = {
                viewModel.onIntent(ProfileIntent.OpenDialog(false))
                context.navigateToAppSettings()
                viewModel.onIntent(ProfileIntent.UpdatePhotoPermission(true)) // todo : 확인 요망
            },
            onDismissRequest = { viewModel.onIntent(ProfileIntent.OpenDialog(false)) },
        )
    }

    ProfileScreen(
        profileState = profileState,
        profileEditType = ProfileEditType.ONBOARDING,
        onNextBtnClick = { nickname, imageUri, defaultImage ->
            memberInfoEditModel = memberInfoEditModel.copy(
                nickname = nickname,
                memberDefaultProfileImage = defaultImage.orEmpty(),
            )
            viewModel.navigateToAgreeTerms()
            trackEvent(CLICK_NEXT_PROFILE_SIGNUP)
        },
        onProfilePlusBtnClick = {
            viewModel.requestImagePicker()
            trackEvent(CLICK_ADD_PICTURE_PROFILE_SIGNUP)
        },
        onDuplicateBtnClick = { viewModel.onIntent(ProfileIntent.GetNickNameValidation) },
        onRandomImageChange = { newImage ->
            viewModel.onIntent(ProfileIntent.OnRandomImageChange(newImage))
            viewModel.onIntent(ProfileIntent.OnImageSelected(null))
            trackEvent(CLICK_CHANGE_PICTURE_PROFILE_SIGNUP)
        },
        onNicknameChange = { newNickname ->
            viewModel.onIntent(ProfileIntent.OnNicknameChanged(newNickname))
        },
    )
}

@Composable
fun ProfileScreen(
    profileState: ProfileState,
    profileEditType: ProfileEditType,
    onProfilePlusBtnClick: () -> Unit = {},
    onDuplicateBtnClick: () -> Unit = {},
    onRandomImageChange: (ProfileImageType) -> Unit = {},
    onNicknameChange: (String) -> Unit = {}, // 닉네임 변경 핸들러
    onNextBtnClick: (String, String?, String?) -> Unit,
) {
    val focusManager = LocalFocusManager.current // FocusManager를 가져옵니다.

    Column(
        verticalArrangement = Arrangement.SpaceBetween, // 상단과 하단을 공간으로 분리
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus() // 빈 화면을 터치하면 포커스를 해제합니다.
                    })
                },
        ) {
            Text(
                text = stringResource(profileEditType.title),
                style = WableTheme.typography.head00,
                color = WableTheme.colors.black,
                modifier = Modifier.padding(top = 16.dp),
            )

            Text(
                text = stringResource(profileEditType.description),
                style = WableTheme.typography.body02,
                color = WableTheme.colors.gray600,
                modifier = Modifier.padding(top = 6.dp),
            )

            ProfileImagePicker(
                selectedImageUri = profileState.selectedImageUri,
                currentImage = profileState.currentImage,
                onRandomImageChange = onRandomImageChange,
                onProfilePlusBtnClick = onProfilePlusBtnClick,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .size(172.dp)
                    .align(Alignment.CenterHorizontally),
            )

            WableBasicTextField(
                placeholder = stringResource(R.string.profile_edit_nickname_placeholder),
                textFieldType = profileState.textFieldType,
                value = profileState.nickname,
                onValueChange = onNicknameChange,
                modifier = Modifier.padding(top = 28.dp),
            ) {
                WableSmallButton(
                    text = stringResource(R.string.profile_edit_btn_duplicate),
                    onClick = {
                        focusManager.clearFocus()
                        onDuplicateBtnClick()
                    },
                    enabled = profileState.textFieldType != NicknameType.INVALID && profileState.nickname.isNotEmpty(),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }

        WableButton(
            text = stringResource(profileEditType.buttonText),
            onClick = {
                onNextBtnClick(
                    profileState.nickname,
                    profileState.selectedImageUri,
                    if (profileState.selectedImageUri != null) null else profileState.currentImage.name,
                )
            },
            enabled = profileState.textFieldType == NicknameType.CORRECT,
            modifier = Modifier.padding(bottom = 24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        ProfileScreen(
            onNextBtnClick = { _, _, _ -> },
            onProfilePlusBtnClick = {},
            profileState = ProfileState(),
            profileEditType = ProfileEditType.PROFILE,
        )
    }
}
