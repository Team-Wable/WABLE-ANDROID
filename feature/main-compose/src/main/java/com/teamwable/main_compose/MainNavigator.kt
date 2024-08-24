package com.teamwable.main_compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.teamwable.auth.naviagation.navigateLogin
import com.teamwable.navigation.Route
import com.teamwable.onboarding.firstlckwatch.naviagation.navigateToFirstLckWatch
import com.teamwable.onboarding.profile.naviagation.navigateToProfile
import com.teamwable.onboarding.selectlckteam.naviagation.navigateToSelectLckTeam

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Login

    fun navigateToLogin(navOptions: NavOptions) {
        navController.navigateLogin(navOptions)
    }

    fun navigateToFirstLckWatch() {
        navController.navigateToFirstLckWatch()
    }

    fun navigateToSelectLckTeam(userList: List<String>) {
        navController.navigateToSelectLckTeam(userList)
    }

    fun navigateToProfile(userList: List<String>) {
        navController.navigateToProfile(userList)
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStackIfNotLogin() {
        if (!isSameCurrentDestination<Route.Login>()) {
            popBackStack()
        }
    }

    fun resetToLogin() {
        val navOptions = navOptions {
            popUpTo(startDestination) {
                saveState = true
            }
            launchSingleTop = true
        }

        navController.navigateLogin(navOptions)
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
