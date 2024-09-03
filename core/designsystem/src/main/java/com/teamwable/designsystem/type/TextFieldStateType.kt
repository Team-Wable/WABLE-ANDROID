package com.teamwable.designsystem.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.teamwable.designsystem.theme.WableTheme

enum class TextFieldStateType {
    DEFAULT,
    ERROR,
    SUCCESS,
    ;

    @Composable
    fun labelTextColor(): Color = when (this) {
        DEFAULT -> WableTheme.colors.gray600
        ERROR -> WableTheme.colors.error
        SUCCESS -> WableTheme.colors.success
    }
}
