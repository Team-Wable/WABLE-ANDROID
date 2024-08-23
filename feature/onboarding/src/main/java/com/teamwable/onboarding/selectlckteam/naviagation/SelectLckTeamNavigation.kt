package com.teamwable.onboarding.selectlckteam.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.teamwable.navigation.Route
import com.teamwable.onboarding.selectlckteam.SelectLckTeamRoute

fun NavController.navigateToSelectLckTeam(userList: List<String>) {
    this.navigate(Route.SelectLckTeam(userList))
}

fun NavGraphBuilder.selectLckTeamNavGraph(
    navigateToSelectLckTeam: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.SelectLckTeam> { backStackEntry ->
        val args = backStackEntry.toRoute<Route.SelectLckTeam>()
        SelectLckTeamRoute(
            navigateToSelectLckTeam = navigateToSelectLckTeam,
            onShowErrorSnackBar = onShowErrorSnackBar,
            args = args,
        )
    }
}
