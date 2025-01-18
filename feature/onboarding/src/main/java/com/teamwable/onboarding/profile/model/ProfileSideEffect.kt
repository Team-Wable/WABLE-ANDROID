package com.teamwable.onboarding.profile.model

import com.teamwable.common.base.SideEffect

sealed interface ProfileSideEffect : SideEffect {
    data object RequestImagePicker : ProfileSideEffect

    data object ShowPermissionDeniedDialog : ProfileSideEffect

    data object NavigateToAgreeTerms : ProfileSideEffect

    data class ShowSnackBar(val message: Throwable) : ProfileSideEffect

    data object NavigateToProfile : ProfileSideEffect
}
