package com.teamwable.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalWableColors = staticCompositionLocalOf<WableColors> {
    error("No WableColors provided")
}

private val LocalWableTypography = staticCompositionLocalOf<WableTypography> {
    error("No WableTypography provided")
}

/*
* WableTheme
*
* Color에 접근하고 싶을때 WableTheme.colors.primary 이런식으로 접근하면 됩니다.
* Typo를 변경하고 싶다면 WableTheme.typography.h1 이런식으로 접근하면 됩니다.
* */

object WableTheme {
    val colors: WableColors
        @Composable
        @ReadOnlyComposable
        get() = LocalWableColors.current

    val typography: WableTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalWableTypography.current
}

@Composable
fun ProvideWableColorsAndTypography(colors: WableColors, typography: WableTypography, content: @Composable () -> Unit) {
    val provideColors = remember { colors.copy() }
    provideColors.update(colors)

    val provideTypography = remember { typography.copy() }
    provideTypography.update(typography)

    CompositionLocalProvider(
        LocalWableColors provides provideColors,
        LocalWableTypography provides provideTypography,
        content = content,
    )
}

@Composable
fun WableTheme(
    content: @Composable () -> Unit,
) {
    val colors = wableLightColors()
    val typography = wableTypography()
    ProvideWableColorsAndTypography(colors, typography) {
        MaterialTheme(
            content = content, colorScheme = lightColorScheme(colors),
        )
    }
}

@Composable
private fun lightColorScheme(colors: WableColors): ColorScheme =
    lightColorScheme(
        primary = colors.purple50.copy(alpha = 0.99f),
        background = colors.white,
        surface = colors.gray100,
        error = colors.error,
    )
