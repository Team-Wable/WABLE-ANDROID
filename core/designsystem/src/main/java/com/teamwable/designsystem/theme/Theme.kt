package com.teamwable.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.teamwable.designsystem.extension.system.SetLightNavigationBar
import com.teamwable.designsystem.extension.system.SetStatusBarColor

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
    SetStatusBarColor(color = colors.white)
    SetLightNavigationBar()
    ProvideSoptColorsAndTypography(colors, typography) {
        MaterialTheme(content = content)
    }
}
