package com.teamwable.main_compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.teamwable.auth.naviagation.navigateLogin
import com.teamwable.navigation.Route

internal class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Login

    fun navigateToLogin(navOptions: NavOptions) {
        navController.navigateLogin(navOptions)
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStackIfNotLogin() {
        if (!isSameCurrentDestination<Route.Login>()) {
            popBackStack()
        }
    }

    fun isBackStackNotEmpty(): Boolean = navController.previousBackStackEntry != null

    fun navigateUp() {
        navController.navigateUp()
    }

    @Composable
    fun shouldShowTopBar(): Boolean {
        val currentRoute = currentDestination?.route ?: return false
        return !(currentRoute.contains("Login") || currentRoute.contains("Splash"))
    }

    private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
