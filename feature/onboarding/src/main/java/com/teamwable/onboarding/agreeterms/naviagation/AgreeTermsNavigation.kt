package com.teamwable.onboarding.agreeterms.naviagation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.navigation.Route
import com.teamwable.navigation.parcelableNavType
import com.teamwable.onboarding.agreeterms.AgreeTermsRoute
import kotlin.reflect.typeOf

fun NavController.navigateToAgreeTerms(
    memberInfoEditModel: MemberInfoEditModel,
    profileUri: String?,
) {
    this.navigate(Route.AgreeTerms(memberInfoEditModel, profileUri))
}

fun NavGraphBuilder.agreeTermsNavGraph(
    navigateToHome: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<Route.AgreeTerms>(
        typeMap = mapOf(
            typeOf<MemberInfoEditModel>() to parcelableNavType<MemberInfoEditModel>(),
        ),
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<Route.AgreeTerms>()
        AgreeTermsRoute(
            navigateToHome = navigateToHome,
            onShowErrorSnackBar = onShowErrorSnackBar,
            args = args,
        )
    }
}
