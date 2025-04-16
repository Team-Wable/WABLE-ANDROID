package com.teamwable.viewit.ui

import com.teamwable.common.base.SideEffect

sealed interface ViewItSideEffect : SideEffect {
    data object NavigateToProfile : ViewItSideEffect

    data class ShowSnackBar(val message: String) : ViewItSideEffect
}
