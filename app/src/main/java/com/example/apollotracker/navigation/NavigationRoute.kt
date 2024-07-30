package com.example.apollotracker.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRoute

@Serializable
data object Splash : NavigationRoute

@Serializable
data object Main :  NavigationRoute

@Serializable
data object Altcoin : NavigationRoute

@Serializable
data object Settings : NavigationRoute

@Serializable
data object Back : NavigationRoute