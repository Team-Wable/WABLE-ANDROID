package com.teamwable.designsystem.extension

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

@Composable
fun SetStatusBarColor(color: Int) {
    val activity = LocalContext.current as? Activity
    SideEffect {
        activity?.window?.let { window ->
            WindowCompat.getInsetsController(
                window,
                window.decorView,
            ).isAppearanceLightStatusBars = false
            window.statusBarColor = color
        }
    }
}
