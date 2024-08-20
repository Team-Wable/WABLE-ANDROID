package com.teamwable.auth.model

import com.teamwable.designsystem.uistate.UiState

data class KakaoLoginState(
    val state: UiState<Boolean> = UiState.Loading,
)
