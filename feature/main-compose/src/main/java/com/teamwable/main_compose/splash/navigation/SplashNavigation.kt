package com.teamwable.main_compose.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamwable.main_compose.splash.SplashRoute
import com.teamwable.navigation.Route

fun NavGraphBuilder.splashNavGraph(
    navigateToLogIn: () -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<Route.Splash> {
        SplashRoute(
            navigateToHome = navigateToHome,
            navigateToLogIn = navigateToLogIn,
        )
    }
}
