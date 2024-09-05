package com.teamwable.designsystem.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableCustomCardWithStroke(
    connerRadius: Dp = 8.dp,
    strokeWidth: Dp = 1.dp,
    strokeColor: Color = WableTheme.colors.gray300,
    enabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(connerRadius),
        colors = CardDefaults.cardColors(
            containerColor = WableTheme.colors.white,
        ),
        border = BorderStroke(
            width = strokeWidth,
            color = if (enabled) WableTheme.colors.purple50 else WableTheme.colors.gray300,
        ),
    ) {
        content()
    }
}
