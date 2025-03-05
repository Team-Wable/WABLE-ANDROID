package com.teamwable.designsystem.type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.button.ButtonDefault
import com.teamwable.designsystem.theme.WableTheme

@Stable
data class SmallButtonStyle(
    override val radius: Dp = 8.dp,
    override val textStyle: TextStyle,
    override val backgroundColor: @Composable (Boolean) -> Color,
    override val textColor: @Composable (Boolean) -> Color,
    val minHeight: Dp = 0.dp,
    val paddingVertical: Dp = 13.dp,
    val paddingHorizontal: Dp = 16.dp,
) : ButtonDefault

object SmallButtonDefaults {
    @Composable
    @ReadOnlyComposable
    fun defaultSmallButtonStyle() = SmallButtonStyle(
        radius = 8.dp,
        paddingVertical = 13.dp,
        paddingHorizontal = 16.dp,
        textStyle = WableTheme.typography.body03,
        backgroundColor = { enabled -> if (enabled) WableTheme.colors.gray900 else WableTheme.colors.gray200 },
        textColor = { enabled -> if (enabled) WableTheme.colors.gray100 else WableTheme.colors.gray600 },
    )

    @Composable
    @ReadOnlyComposable
    fun miniButtonStyle() = defaultSmallButtonStyle().copy(
        paddingVertical = 6.dp,
        paddingHorizontal = 14.dp,
        radius = 20.dp,
        textColor = { WableTheme.colors.white },
        backgroundColor = { WableTheme.colors.gray900 },
    )
}
