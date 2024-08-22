package com.teamwable.main_compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.teamwable.auth.naviagation.loginNavGraph
import com.teamwable.common.intentprovider.IntentProvider
import com.teamwable.designsystem.component.topbar.WableAppBar
import com.teamwable.main_compose.splash.navigation.splashNavGraph
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    intentProvider: IntentProvider,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val localContext = LocalContext.current
    val intent = intentProvider.getIntent()

    val coroutineScope = rememberCoroutineScope()
    val localContextResource = LocalContext.current.resources
    val onShowErrorSnackBar: (throwable: Throwable?) -> Unit = { throwable ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(
                when (throwable) {
                    is UnknownHostException -> localContextResource.getString(R.string.error_message_network)
                    else -> localContextResource.getString(R.string.error_message_unknown)
                },
            )
        }
    }

    Scaffold(
        topBar = {
            WableAppBar(
                visibility = navigator.shouldShowTopBar(),
                canNavigateBack = navigator.isBackStackNotEmpty(),
                navigateUp = { navigator.navigateUp() },
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                NavHost(
                    navController = navigator.navController,
                    startDestination = navigator.startDestination,
                ) {
                    splashNavGraph(
                        navigateToLogIn = {
                            val navOptions = navOptions {
                                popUpTo(navigator.navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                            navigator.navigateToLogin(
                                navOptions = navOptions,
                            )
                        },
                        navigateToHome = { startActivity(localContext, intent, null) },
                        intentProvider = intentProvider,
                    )
                    loginNavGraph(
                        navigateToFirstLckWatch = { navigator.navigateToFirstLckWatch() },
                        navigateToHome = { startActivity(localContext, intent, null) },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    )
}
