package com.teamwable.onboarding.selectlckteam.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamwable.navigation.Route
import com.teamwable.onboarding.selectlckteam.SelectLckTeamRoute

fun NavController.navigateToSelectLckTeam() {
    this.navigate(Route.SelectLckTeam)
}

fun NavGraphBuilder.selectLckTeamNavGraph(
    navigateToSelectLckTeam: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.SelectLckTeam> {
        SelectLckTeamRoute(
            navigateToSelectLckTeam = navigateToSelectLckTeam,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
