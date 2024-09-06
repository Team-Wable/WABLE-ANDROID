package com.teamwable.onboarding.profile.model

sealed interface ProfileSideEffect {
    data object RequestImagePicker : ProfileSideEffect

    data object ShowPermissionDeniedDialog : ProfileSideEffect

    data object NavigateToAgreeTerms : ProfileSideEffect

    data class ShowSnackBar(val message: Throwable) : ProfileSideEffect

    data object NavigateToProfile : ProfileSideEffect
}
