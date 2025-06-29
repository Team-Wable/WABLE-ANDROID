package com.teamwable.profile.profile.edit

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.util.getThrowableMessage
import com.teamwable.designsystem.component.button.SmallButtonDefaults
import com.teamwable.designsystem.component.button.WableSmallButton
import com.teamwable.designsystem.component.snackbar.SNACK_BAR_DURATION
import com.teamwable.designsystem.component.snackbar.WableSnackBar
import com.teamwable.designsystem.component.snackbar.WableSnackBarPopUp
import com.teamwable.designsystem.component.textfield.WableBasicTextField
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.NicknameType
import com.teamwable.designsystem.type.ProfileImageType
import com.teamwable.designsystem.type.SnackBarType
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.onboarding.R
import com.teamwable.onboarding.profile.component.ProfileImagePicker
import com.teamwable.onboarding.profile.model.ProfileSideEffect
import com.teamwable.onboarding.profile.permission.launchImagePicker
import com.teamwable.onboarding.profile.permission.rememberGalleryLauncher
import com.teamwable.onboarding.profile.permission.rememberPhotoPickerLauncher
import com.teamwable.onboarding.selectlckteam.WableSelectLckTeamGrid
import com.teamwable.profile.profile.edit.model.ProfileEditState
import com.teamwable.profile.profile.edit.model.ProfilePatchState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ProfileEditRoute(
    viewModel: ProfileEditViewModel = hiltViewModel(),
    profile: MemberInfoEditModel,
    navigateUp: () -> Unit,
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
        viewModel.onSelectTeamChange(profile.memberFanTeam.toString())
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
                            memberFanTeam = profileState.selectedTeam?.name,
                        ),
                    )

                    is ProfileSideEffect.RequestImagePicker -> context.launchImagePicker(galleryLauncher, photoPickerLauncher)
                    is ProfileSideEffect.ShowSnackBar -> onShowErrorSnackBar(sideEffect.message)

                    else -> Unit
                }
            }
    }

    ProfileEditScreen(
        profileState = profileState,
        onNextBtnClick = { nickname, imageUri, defaultImage ->
            if (profileState.textFieldType == NicknameType.CORRECT || profileState.textFieldType == NicknameType.DEFAULT) {
                viewModel.patchUserProfile(
                    memberInfoEditModel = profile.copy(
                        nickname = nickname,
                        memberDefaultProfileImage = defaultImage,
                        memberFanTeam = profileState.selectedTeam.name,
                    ),
                    imgUrl = imageUri,
                )
                viewModel.onImageSelected(imageUri ?: defaultImage)
            }
        },
        onProfilePlusBtnClick = viewModel::requestImagePicker,
        onDuplicateBtnClick = viewModel::getNickNameValidation,
        onRandomImageChange = { newImage ->
            viewModel.onRandomImageChange(newImage)
            viewModel.onImageSelected(null)
        },
        onNicknameChange = viewModel::onNicknameChanged,
        onSelectTeamChange = viewModel::onSelectTeamChange,
        navigateUp = navigateUp,
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

@Composable
private fun ProfileEditScreen(
    profileState: ProfileEditState,
    onProfilePlusBtnClick: () -> Unit = {},
    onDuplicateBtnClick: () -> Unit = {},
    onRandomImageChange: (ProfileImageType) -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onSelectTeamChange: (String?) -> Unit = {},
    onNextBtnClick: (String, String?, String?) -> Unit,
    navigateUp: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterStart),
            ) {
                IconButton(
                    onClick = navigateUp,
                    modifier = Modifier.padding(start = 4.dp),
                ) {
                    Icon(
                        painter = painterResource(id = com.teamwable.common.R.drawable.ic_share_back_btn),
                        contentDescription = "",
                    )
                }
            }

            Text(
                text = "프로필 편집",
                style = WableTheme.typography.body01,
                color = WableTheme.colors.black,
                modifier = Modifier.align(Alignment.Center),
            )

            WableSmallButton(
                text = "완료",
                buttonStyle = SmallButtonDefaults.miniButtonStyle().copy(
                    backgroundColor = { WableTheme.colors.purple50 },
                ),
                onClick = {
                    onNextBtnClick(
                        profileState.nickname,
                        profileState.selectedImageUri,
                        if (profileState.selectedImageUri != null) null else profileState.currentImage.name,
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
            )
        }

        WableSelectLckTeamGrid(
            content = {
                Column(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus() // 빈 화면을 터치하면 포커스를 해제합니다.
                            })
                        },
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    ProfileImagePicker(
                        selectedImageUri = profileState.selectedImageUri,
                        currentImage = profileState.currentImage,
                        onRandomImageChange = onRandomImageChange,
                        onProfilePlusBtnClick = onProfilePlusBtnClick,
                        modifier = Modifier
                            .size(172.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                    Text(
                        text = "닉네임",
                        style = WableTheme.typography.body03,
                        color = WableTheme.colors.black,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    WableBasicTextField(
                        placeholder = stringResource(R.string.profile_edit_nickname_placeholder),
                        textFieldType = profileState.textFieldType,
                        value = profileState.nickname,
                        onValueChange = onNicknameChange,
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
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "응원팀",
                        style = WableTheme.typography.body03,
                        color = WableTheme.colors.black,
                    )
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp),
            shuffledTeams = profileState.shuffledTeams,
            selectedTeam = profileState.selectedTeam,
            onTeamSelected = {
                if (it != null) onSelectTeamChange(it.name)
                else null
            onTeamSelected = { onSelectTeamChange(it?.name) },
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileEditPreview() {
    WableTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ProfileEditScreen(
                onNextBtnClick = { _, _, _ -> },
                onProfilePlusBtnClick = {},
                profileState = ProfileEditState(),
            )
        }
    }
}
