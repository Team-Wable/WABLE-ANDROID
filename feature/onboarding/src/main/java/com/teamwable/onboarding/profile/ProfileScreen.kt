package com.teamwable.onboarding.profile

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.navigation.Route
import com.teamwable.onboarding.R
import com.teamwable.onboarding.selectlckteam.SelectLckTeamViewModel
import timber.log.Timber

@Composable
fun ProfileRoute(
    viewModel: SelectLckTeamViewModel = hiltViewModel(),
    args: Route.Profile,
    navigateToProfile: (List<String>) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var userMutableList by remember { mutableStateOf(args.userList) }

    Timber.d("ProfileRoute: $userMutableList")

    /* LaunchedEffect(lifecycleOwner) {
         viewModel.firstLckWatchSideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
             .collect { sideEffect ->
                 when (sideEffect) {
                     is SelectLckTeamSideEffect.NavigateToProfile -> navigateToProfile(userMutableList)
                     else -> Unit
                 }
             }
     }*/

    ProfileScreen(onNextBtnClick = {})
}

@Composable
fun ProfileScreen(
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
                    imageModel = { },
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
                        },
                )
                Image(
                    painter = painterResource(id = com.teamwable.common.R.drawable.ic_sign_up_profile_plus_btn),
                    contentDescription = "Plus Profile Image",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .noRippleClickable { },
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
        )
    }
}
