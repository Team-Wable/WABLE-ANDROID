package com.teamwable.onboarding.profile

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.dialog.PermissionAppSettingsDialog
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.extension.system.navigateToAppSettings
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.ProfileImageType
import com.teamwable.navigation.Route
import com.teamwable.onboarding.R
import com.teamwable.onboarding.permission.launchImagePicker
import com.teamwable.onboarding.permission.rememberGalleryLauncher
import com.teamwable.onboarding.permission.rememberPhotoPickerLauncher
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

    val selectedImageUri by viewModel.selectedImageUri.collectAsStateWithLifecycle() // UI 상태 구독
    var openDialog by remember { mutableStateOf(false) }
    var currentImage by remember { mutableStateOf(ProfileImageType.entries.random()) }

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
        onNextBtnClick = {},
        onProfilePlusBtnClick = { viewModel.requestImagePicker() },
        selectedImageUri = selectedImageUri,
        currentImage = currentImage,
        onRandomImageChange = { newImage ->
            currentImage = newImage
            viewModel.onImageSelected(null)
        },
    )
}

@Composable
fun ProfileScreen(
    onProfilePlusBtnClick: () -> Unit = {},
    selectedImageUri: String? = null,
    currentImage: ProfileImageType, // 현재 이미지를 받는 파라미터 추가
    onRandomImageChange: (ProfileImageType) -> Unit = {},
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 78.dp, end = 78.dp),
            ) {
                GlideImage(
                    imageModel = { selectedImageUri ?: currentImage.image },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                    ),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape),
                    previewPlaceholder = painterResource(id = com.teamwable.common.R.drawable.ic_share_profile_img_blue),
                )
                Image(
                    painter = painterResource(id = com.teamwable.common.R.drawable.ic_sign_up_profile_change_btn),
                    contentDescription = "Change Profile Image",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .noRippleClickable {
                            val newImage = ProfileImageType.entries
                                .filter { it != currentImage }
                                .random()
                            onRandomImageChange(newImage)
                        },
                )
                Image(
                    painter = painterResource(id = com.teamwable.common.R.drawable.ic_sign_up_profile_plus_btn),
                    contentDescription = "Plus Profile Image",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .noRippleClickable {
                            onProfilePlusBtnClick()
                        },
                )
            }
        }

        WableButton(
            text = stringResource(R.string.btn_next_text),
            onClick = {},
            enabled = false,
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
        )
    }
}
