package com.teamwable.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.auth.model.LoginSideEffect
import com.teamwable.designsystem.component.dialog.WableButtonDialog
import com.teamwable.designsystem.extension.modifier.noRippleDebounceClickable
import com.teamwable.designsystem.extension.system.SetStatusBarColor
import com.teamwable.designsystem.theme.SystemLoginSystemAppBar
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.DialogType

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToFirstLckWatch: () -> Unit,
    navigateToHome: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.observeAutoLogin()
    }

    LaunchedEffect(lifecycleOwner) {
        viewModel.loginSideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is LoginSideEffect.NavigateToMain -> navigateToHome()
                    is LoginSideEffect.NavigateToFirstLckWatch -> navigateToFirstLckWatch()
                    is LoginSideEffect.ShowSnackBar -> onShowErrorSnackBar(sideEffect.message)
                }
            }
    }

    if (showDialog) {
        WableButtonDialog(
            dialogType = DialogType.LOGIN,
            onClick = {
                viewModel.startKaKaoLogin(context)
            },
            onDismissRequest = { viewModel.showLoginDialog(false) },
        )
    }

    LoginScreen(
        onLoginBtnClick = {
            viewModel.showLoginDialog(true)
        },
    )
}

@Composable
fun LoginScreen(
    onLoginBtnClick: () -> Unit,
) {
    SetStatusBarColor(color = SystemLoginSystemAppBar)

    val configuration = LocalConfiguration.current
    val screenHeightPx = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
    val halfScreenHeightPx = screenHeightPx * 0.514f

    val colorStops = arrayOf(
        0.0f to Color(0xFFEBE2FD),
        0.37f to Color(0xFFF0F6FE),
        0.69f to Color(0xFFF7FEFD),
        1f to WableTheme.colors.white,
    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = colorStops,
                    tileMode = TileMode.Decal,
                    startY = 0f,
                    endY = halfScreenHeightPx,
                ),
            ),
    ) {
        Image(
            painter = painterResource(id = com.teamwable.common.R.drawable.ic_share_logo),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 50.dp)
                .align(alignment = Alignment.CenterHorizontally),
        )

        Text(
            modifier = Modifier
                .padding(top = 26.dp)
                .align(alignment = Alignment.CenterHorizontally),
            text = stringResource(R.string.login_descrption),
            style = WableTheme.typography.head00,
            color = WableTheme.colors.black,
            textAlign = Center,
        )

        Image(
            painter = painterResource(id = R.drawable.img_login_background),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 59.dp)
                .fillMaxWidth()
                .aspectRatio(1.2815f)
                .align(alignment = Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = com.teamwable.common.R.drawable.ic_login_kakao_btn),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 54.dp)
                .aspectRatio(6.56f)
                .noRippleDebounceClickable { onLoginBtnClick() },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        LoginScreen(
            onLoginBtnClick = {},
        )
    }
}
