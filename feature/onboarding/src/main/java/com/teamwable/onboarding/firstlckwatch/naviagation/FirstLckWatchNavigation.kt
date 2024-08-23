package com.teamwable.onboarding.firstlckwatch.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamwable.navigation.Route
import com.teamwable.onboarding.firstlckwatch.FirstLckWatchRoute

fun NavController.navigateToFirstLckWatch() {
    this.navigate(Route.FirstLckWatch)
}

fun NavGraphBuilder.firstLckWatchNavGraph(
    navigateToSelectLckTeam: (userList: List<String>) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.FirstLckWatch> {
        FirstLckWatchRoute(
            navigateToSelectLckTeam = navigateToSelectLckTeam,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
