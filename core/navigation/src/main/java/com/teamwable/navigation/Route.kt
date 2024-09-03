package com.teamwable.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object FirstLckWatch : Route

    @Serializable
    data class SelectLckTeam(val userList: List<String>) : Route

    @Serializable
    data class Profile(val userList: List<String>) : Route

    @Serializable
    data class AgreeTerms(val userList: List<String>) : Route
}
