package com.teamwable.main_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.teamwable.common.di.MAIN
import com.teamwable.common.intentprovider.IntentProvider
import com.teamwable.designsystem_compose.theme.WableTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {
    @MAIN
    @Inject
    lateinit var intentProvider: IntentProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WableTheme {
                val navigator: MainNavigator = rememberMainNavigator()
                MainScreen(
                    navigator = navigator,
                    intentProvider = intentProvider,
                )
            }
        }
    }
}
