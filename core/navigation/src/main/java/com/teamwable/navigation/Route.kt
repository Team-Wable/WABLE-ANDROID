package com.teamwable.navigation

import com.teamwable.model.profile.MemberInfoEditModel
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object FirstLckWatch : Route

    @Serializable
    data class SelectLckTeam(val memberInfoEditModel: MemberInfoEditModel) : Route

    @Serializable
    data class Profile(val memberInfoEditModel: MemberInfoEditModel) : Route

    @Serializable
    data class AgreeTerms(val memberInfoEditModel: MemberInfoEditModel, val profileUri: String?) : Route
}
