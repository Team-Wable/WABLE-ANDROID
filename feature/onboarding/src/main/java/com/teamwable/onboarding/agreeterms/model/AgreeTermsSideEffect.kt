package com.teamwable.onboarding.agreeterms.model

sealed interface AgreeTermsSideEffect {
    data object NavigateToHome : AgreeTermsSideEffect

    data object ShowDialog : AgreeTermsSideEffect

    data class ShowSnackBar(val message: Throwable) : AgreeTermsSideEffect
}
