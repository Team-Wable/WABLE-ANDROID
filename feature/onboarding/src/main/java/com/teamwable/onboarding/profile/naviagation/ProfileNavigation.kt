package com.teamwable.onboarding.profile.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.teamwable.navigation.Route
import com.teamwable.onboarding.profile.ProfileRoute

fun NavController.navigateToProfile(userList: List<String>) {
    this.navigate(Route.Profile(userList))
}

fun NavGraphBuilder.profileNavGraph(
    navigateToAgreeTerms: (List<String>) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.Profile> { backStackEntry ->
        val args = backStackEntry.toRoute<Route.Profile>()
        ProfileRoute(
            navigateToAgreeTerms = navigateToAgreeTerms,
            onShowErrorSnackBar = onShowErrorSnackBar,
            args = args,
        )
    }
}
