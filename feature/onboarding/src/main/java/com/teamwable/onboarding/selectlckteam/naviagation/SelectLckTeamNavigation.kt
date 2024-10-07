package com.teamwable.onboarding.selectlckteam.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.navigation.Route
import com.teamwable.navigation.parcelableNavType
import com.teamwable.onboarding.selectlckteam.SelectLckTeamRoute
import kotlin.reflect.typeOf

fun NavController.navigateToSelectLckTeam(memberInfoEdit: MemberInfoEditModel) {
    this.navigate(Route.SelectLckTeam(memberInfoEdit))
}

fun NavGraphBuilder.selectLckTeamNavGraph(
    navigateToProfile: (MemberInfoEditModel) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.SelectLckTeam>(
        typeMap = mapOf(
            typeOf<MemberInfoEditModel>() to parcelableNavType<MemberInfoEditModel>(),
        ),
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<Route.SelectLckTeam>()
        SelectLckTeamRoute(
            navigateToProfile = navigateToProfile,
            onShowErrorSnackBar = onShowErrorSnackBar,
            args = args,
        )
    }
}
