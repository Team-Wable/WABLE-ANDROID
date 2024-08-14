package com.teamwable.main_compose.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamwable.common.intentprovider.IntentProvider
import com.teamwable.navigation.Route

fun NavGraphBuilder.splashNavGraph(
    navigateToLogIn: () -> Unit,
    navigateToHome: () -> Unit,
    intentProvider: IntentProvider,
) {
    composable<Route.Splash> {
        /*  SplashScreen(
              navigateToHome = navigateToHome,
              navigateToLogIn = navigateToLogIn
              intentProvider: IntentProvider,
          )*/
    }
}
