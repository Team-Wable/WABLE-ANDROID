package com.teamwable.onboarding.firstlckwatch.model

sealed interface FirstLckWatchSideEffect {
    data object NavigateToSelectLckTeam : FirstLckWatchSideEffect

    data class ShowSnackBar(val message: String) : FirstLckWatchSideEffect
}
