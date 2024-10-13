package com.teamwable.main_compose.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.main_compose.R
import com.teamwable.main_compose.splash.model.SplashSideEffect
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToLogIn: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wable_splash))

    LaunchedEffect(Unit) {
        delay(2000)
        viewModel.observeAutoLogin()
    }

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is SplashSideEffect.NavigateToHome -> navigateToHome()
                    is SplashSideEffect.NavigateToLogin -> navigateToLogIn()
                }
            }
    }

    WableSplashScreen(
        composition = composition,
    )
}

@Composable
fun WableSplashScreen(
    composition: LottieComposition?,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        WableSplashScreen(
            composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wable_splash)).value,
        )
    }
}
