package com.teamwable.onboarding.agreeterms.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.teamwable.navigation.Route
import com.teamwable.onboarding.agreeterms.AgreeTermsRoute

fun NavController.navigateToAgreeTerms(userList: List<String>) {
    this.navigate(Route.AgreeTerms(userList))
}

fun NavGraphBuilder.agreeTermsNavGraph(
    navigateToHome: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.AgreeTerms> { backStackEntry ->
        val args = backStackEntry.toRoute<Route.AgreeTerms>()
        AgreeTermsRoute(
            navigateToHome = navigateToHome,
            onShowErrorSnackBar = onShowErrorSnackBar,
            args = args,
        )
    }
}
