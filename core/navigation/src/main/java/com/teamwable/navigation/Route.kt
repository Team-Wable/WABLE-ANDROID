package com.teamwable.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object FIRST : Route

    @Serializable
    data object SECOND : Route

    @Serializable
    data class THIRD(val id: String) : Route

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Home : Route
}
