package com.example.apollotracker.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRoute {
    val isSplash: Boolean get() = this is Splash
    val route: String get() = this::class.qualifiedName.orEmpty()
    companion object {
        fun fromString(route: String?): NavigationRoute = when (route) {
            Splash.route -> Splash
            Main.route -> Main
            Altcoin.route -> Altcoin
            Settings.route -> Settings
            Graph.route -> Graph
            else -> Main
        }
    }
}

@Serializable
data object Splash : NavigationRoute

@Serializable
data object Main :  NavigationRoute

@Serializable
data object Altcoin : NavigationRoute

@Serializable
data object Settings : NavigationRoute

@Serializable
data object Graph : NavigationRoute