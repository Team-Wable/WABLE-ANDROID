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
    data object SelectLckTeam : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data object AgreeTerms : Route
}
