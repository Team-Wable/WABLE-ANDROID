package com.teamwable.designsystem.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableCustomCardWithStroke(
    modifier: Modifier = Modifier,
    connerRadius: Dp = 8.dp,
    strokeWidth: Dp = 1.dp,
    strokeColor: @Composable (Boolean) -> Color = { enabled ->
        if (enabled) WableTheme.colors.purple50 else WableTheme.colors.gray300
    },
    backGroundColor: Color = WableTheme.colors.white,
    enabled: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(connerRadius),
        colors = CardDefaults.cardColors(
            containerColor = backGroundColor,
        ),
        border = BorderStroke(
            width = strokeWidth,
            color = strokeColor(enabled),
        ),
        modifier = modifier.noRippleClickable(onClick = onClick),
    ) {
        content()
    }
}
