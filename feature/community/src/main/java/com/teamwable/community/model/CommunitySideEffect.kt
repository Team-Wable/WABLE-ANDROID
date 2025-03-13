package com.teamwable.community.model

import com.teamwable.common.base.SideEffect

sealed interface CommunitySideEffect : SideEffect {
    data class ShowSnackBar(val throwable: Throwable) : CommunitySideEffect

    data object CopyToClipBoard : CommunitySideEffect

    data object NavigateToGoogleForm : CommunitySideEffect

    data object NavigateToPushAlarm : CommunitySideEffect

    data object ScrollToTop : CommunitySideEffect
}
