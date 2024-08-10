package com.teamwable.auth.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teamwable.navigation.Route

fun NavController.navigateLogin(navOptions: NavOptions) {
    this.navigate(Route.Login, navOptions)
}

fun NavGraphBuilder.loginNavGraph(
    navigateToOnBoarding: () -> Unit,
    navigateToHome: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.Login> {
        /*LoginScreen(
            navigateToHome = navigateToHome,
            navigateToOnBoarding = navigateToOnBoarding
            onShowErrorSnackBar = onShowErrorSnackBar
        )*/
    }
}
