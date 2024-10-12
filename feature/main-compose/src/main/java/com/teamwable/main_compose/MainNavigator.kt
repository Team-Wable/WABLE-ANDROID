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
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.navigation.Route
import com.teamwable.onboarding.agreeterms.naviagation.navigateToAgreeTerms
import com.teamwable.onboarding.firstlckwatch.naviagation.navigateToFirstLckWatch
import com.teamwable.onboarding.profile.naviagation.navigateToProfile
import com.teamwable.onboarding.selectlckteam.naviagation.navigateToSelectLckTeam
import kotlinx.collections.immutable.persistentListOf

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Splash

    private val topBarHiddenRoutes = persistentListOf(
        Route.Login::class, Route.Splash::class,
    )

    fun navigateToLogin(navOptions: NavOptions) {
        navController.navigateLogin(navOptions)
    }

    fun navigateToFirstLckWatch() {
        navController.navigateToFirstLckWatch()
    }

    fun navigateToSelectLckTeam(memberInfoEditModel: MemberInfoEditModel) {
        navController.navigateToSelectLckTeam(memberInfoEditModel)
    }

    fun navigateToProfile(memberInfoEditModel: MemberInfoEditModel) {
        navController.navigateToProfile(memberInfoEditModel)
    }

    fun navigateToAgreeTerms(memberInfoEditModel: MemberInfoEditModel, profileUri: String?) {
        navController.navigateToAgreeTerms(memberInfoEditModel, profileUri)
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
            popUpTo(Route.Login) {
                inclusive = true
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
    fun shouldShowTopBar(): Boolean = !topBarHiddenRoutes.any { currentDestination?.hasRoute(it) ?: false }

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
