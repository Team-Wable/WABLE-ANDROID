package com.teamwable.onboarding.agreeterms.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface AgreeTermState {
    @Immutable
    data object Idle : AgreeTermState

    @Immutable
    data object Loading : AgreeTermState
}
