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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.dialog.PermissionAppSettingsDialog
import com.teamwable.designsystem.component.textfield.WableBasicTextField
import com.teamwable.designsystem.extension.system.navigateToAppSettings
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.NicknameType
import com.teamwable.designsystem.type.ProfileImageType
import com.teamwable.navigation.Route
import com.teamwable.onboarding.R
import com.teamwable.onboarding.permission.launchImagePicker
import com.teamwable.onboarding.permission.rememberGalleryLauncher
import com.teamwable.onboarding.permission.rememberPhotoPickerLauncher
import com.teamwable.onboarding.profile.component.ProfileImagePicker
import com.teamwable.onboarding.profile.model.ProfileSideEffect

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    args: Route.Profile,
    navigateToAgreeTerms: (List<String>) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var userMutableList by remember { mutableStateOf(args.userList) }
    val context = LocalContext.current

    val selectedImageUri by viewModel.selectedImageUri.collectAsStateWithLifecycle()
    var openDialog by remember { mutableStateOf(false) }
    var currentImage by remember { mutableStateOf(ProfileImageType.entries.random()) }
    val nickname by viewModel.nickname.collectAsStateWithLifecycle()
    val textFieldType by viewModel.textFieldType.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (!isGranted) openDialog = true
    }

    LaunchedEffect(Unit) {
        when {
            Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU -> {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }

            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            else -> Unit
        }
    }

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
                    is ProfileSideEffect.NavigateToAgreeTerms -> navigateToAgreeTerms(userMutableList)

                    is ProfileSideEffect.ShowPermissionDeniedDialog -> openDialog = true

                    is ProfileSideEffect.RequestImagePicker -> context.launchImagePicker(galleryLauncher, photoPickerLauncher)

                    else -> Unit
                }
            }
    }

    if (openDialog) {
        PermissionAppSettingsDialog(
            onClick = {
                openDialog = false
                context.navigateToAppSettings()
            },
            onDismissRequest = { openDialog = false },
        )
    }

    ProfileScreen(
        nickname = nickname,
        textFieldType = textFieldType,
        onNextBtnClick = {},
        onProfilePlusBtnClick = { viewModel.requestImagePicker() },
        selectedImageUri = selectedImageUri,
        currentImage = currentImage,
        onRandomImageChange = { newImage ->
            currentImage = newImage
            viewModel.onImageSelected(null)
        },
        onNicknameChange = { newNickname ->
            viewModel.onNicknameChanged(newNickname)
        },
    )
}

@Composable
fun ProfileScreen(
    nickname: String,
    textFieldType: NicknameType,
    onProfilePlusBtnClick: () -> Unit = {},
    selectedImageUri: String? = null,
    currentImage: ProfileImageType,
    onRandomImageChange: (ProfileImageType) -> Unit = {},
    onNicknameChange: (String) -> Unit = {}, // 닉네임 변경 핸들러
    onNextBtnClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween, // 상단과 하단을 공간으로 분리
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Text(
                text = stringResource(R.string.profile_edit_title),
                style = WableTheme.typography.head00,
                color = WableTheme.colors.black,
                modifier = Modifier.padding(top = 16.dp),
            )

            Text(
                text = stringResource(R.string.profile_edit_description),
                style = WableTheme.typography.body02,
                color = WableTheme.colors.gray600,
                modifier = Modifier.padding(top = 6.dp),
            )

            ProfileImagePicker(
                selectedImageUri = selectedImageUri,
                currentImage = currentImage,
                onRandomImageChange = onRandomImageChange,
                onProfilePlusBtnClick = onProfilePlusBtnClick,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .size(172.dp)
                    .align(Alignment.CenterHorizontally),
            )

            WableBasicTextField(
                placeholder = "예) 중꺾마",
                textFieldType = textFieldType,
                value = nickname,
                onValueChange = onNicknameChange,
                modifier = Modifier.padding(top = 28.dp),
            )
        }

        WableButton(
            text = stringResource(R.string.btn_next_text),
            onClick = {},
            enabled = textFieldType == NicknameType.CORRECT,
            modifier = Modifier.padding(bottom = 24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        ProfileScreen(
            onNextBtnClick = {},
            onProfilePlusBtnClick = {},
            selectedImageUri = null,
            currentImage = ProfileImageType.entries.random(),
            nickname = "김민지",
            textFieldType = NicknameType.DEFAULT,
        )
    }
}
