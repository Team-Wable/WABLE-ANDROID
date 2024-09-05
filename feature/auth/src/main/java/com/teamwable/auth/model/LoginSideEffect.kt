package com.teamwable.auth.model

sealed interface LoginSideEffect {
    data object NavigateToMain : LoginSideEffect

    data object NavigateToFirstLckWatch : LoginSideEffect

    data class ShowSnackBar(val message: Throwable) : LoginSideEffect
}
