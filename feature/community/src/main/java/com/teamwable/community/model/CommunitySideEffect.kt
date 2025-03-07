package com.teamwable.community.model

import com.teamwable.common.base.SideEffect

sealed interface CommunitySideEffect : SideEffect {
    data class ShowSnackBar(val message: Throwable) : CommunitySideEffect

    data class CopyToClipBoard(val link: String) : CommunitySideEffect

    data object NavigateToGoogleForm : CommunitySideEffect

    data object NavigateToPushAlarm : CommunitySideEffect
}
