package com.example.apollotracker.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor() {

    private var navigateTo: ((String) -> Unit)? = null

    private val _currentRoute = MutableStateFlow<NavigationRoute>(Splash)
    val currentRoute: StateFlow<NavigationRoute> = _currentRoute.asStateFlow()

    fun setNavigateTo(navigate: (String) -> Unit) {
        navigateTo = navigate
    }

    fun updateCurrentRoute(route: NavigationRoute) {
        _currentRoute.value = route
    }

    fun navigateTo(route: NavigationRoute) {
        navigateTo?.invoke(route.route)
    }
}
