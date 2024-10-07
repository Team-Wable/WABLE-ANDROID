package com.teamwable.onboarding.profile.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.navigation.Route
import com.teamwable.navigation.parcelableNavType
import com.teamwable.onboarding.profile.ProfileRoute
import kotlin.reflect.typeOf

fun NavController.navigateToProfile(memberInfoEditModel: MemberInfoEditModel) {
    this.navigate(Route.Profile(memberInfoEditModel))
}

fun NavGraphBuilder.profileNavGraph(
    navigateToAgreeTerms: (MemberInfoEditModel, String?) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.Profile>(
        typeMap = mapOf(
            typeOf<MemberInfoEditModel>() to parcelableNavType<MemberInfoEditModel>(),
        ),
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<Route.Profile>()
        ProfileRoute(
            navigateToAgreeTerms = navigateToAgreeTerms,
            onShowErrorSnackBar = onShowErrorSnackBar,
            args = args,
        )
    }
}
