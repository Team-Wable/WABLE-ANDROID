package com.teamwable.profile.profile.edit.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface ProfilePatchState {
    @Immutable
    data object Idle : ProfilePatchState

    @Immutable
    data object Loading : ProfilePatchState
}
