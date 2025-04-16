package com.teamwable.viewit.ui

import androidx.compose.runtime.Stable
import com.teamwable.common.base.BaseState

@Stable
data class ViewItUiState(
    val items: String = "",
) : BaseState
