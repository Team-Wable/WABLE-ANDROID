package com.teamwable.main_compose.splash.model

sealed interface SplashSideEffect {
    data object NavigateToHome : SplashSideEffect

    data object NavigateToLogin : SplashSideEffect
}
