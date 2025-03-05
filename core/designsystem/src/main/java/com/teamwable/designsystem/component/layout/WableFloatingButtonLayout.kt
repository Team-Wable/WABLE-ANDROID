package com.teamwable.designsystem.component.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun WableFloatingButtonLayout(
    modifier: Modifier = Modifier,
    buttonAlignment: Alignment = Alignment.BottomCenter,
    buttonContent: @Composable (Modifier) -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
        buttonContent(
            Modifier.align(buttonAlignment),
        )
    }
}
