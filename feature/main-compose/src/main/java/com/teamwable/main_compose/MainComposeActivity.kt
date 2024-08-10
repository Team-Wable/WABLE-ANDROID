package com.teamwable.main_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.teamwable.designsystem_compose.theme.WableTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WableTheme {
                val navigator: MainNavigator = rememberMainNavigator()
                MainScreen(navigator = navigator)
            }
        }
    }
}
