package com.teamwable.designsystem.component.indicator

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableCircularIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    color: Color = WableTheme.colors.info,
    strokeWidth: Dp = 2.dp,
    trackColor: Color = WableTheme.colors.progressBackground,
) {
    CircularProgressIndicator(
        modifier = modifier
            .size(size)
            .padding(5.dp),
        color = color,
        strokeWidth = strokeWidth,
        strokeCap = StrokeCap.Round,
        trackColor = trackColor,
    )
}
