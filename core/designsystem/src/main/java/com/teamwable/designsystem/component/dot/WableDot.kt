package com.teamwable.designsystem.component.dot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableDot(
    color: Color = WableTheme.colors.gray300,
    size: Dp = 16.dp,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
    )
}

@Composable
@Preview(showBackground = true)
fun SimpleDotPreview() {
    WableTheme {
        WableDot()
    }
}
