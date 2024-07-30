package com.example.apollotracker.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor() {

    private val _currentRoute = MutableStateFlow<NavigationRoute>(Splash)
    val currentRoute: StateFlow<NavigationRoute> = _currentRoute.asStateFlow()

    fun navigateTo(route: NavigationRoute) {
        _currentRoute.value = route
    }
}