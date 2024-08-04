package com.example.apollotracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollotracker.navigation.Main
import com.example.apollotracker.navigation.NavigationRoute
import com.example.apollotracker.navigation.Router
import com.example.apollotracker.navigation.Splash
import com.example.apollotracker.store.ModelStore
import com.example.apollotracker.store.StatefulStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApolloTrackerViewModel @Inject constructor(
    private val router: Router
): ViewModel() {

    private val statefulStore: ModelStore<ApolloTrackerAppState> = StatefulStore(ApolloTrackerAppState(), viewModelScope)
    val appState: StateFlow<ApolloTrackerAppState> get() = statefulStore.state

    init {
        viewModelScope.launch {
            router.currentRoute.collect { route ->
                statefulStore.process { oldSate -> oldSate.copy(currentRoute = route) }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.Navigate -> navigateTo(action.route)
            is Action.UpdateCurrentRoute -> updateCurrentRoute(action.route)
            is Action.SetNavigateTo -> setNavigateTo(action.navigate)
        }
    }

    private fun updateCurrentRoute(route: String?) {
        router.updateCurrentRoute(NavigationRoute.fromString(route))
        updateBackArrowVisibility(route)
    }

    private fun updateBackArrowVisibility(route: String?) {
        statefulStore.process { oldState ->
            oldState.copy(
                isBackArrowVisible = route != Main.route
            )
        }
    }

    private fun navigateTo(route: NavigationRoute) {
        router.navigateTo(route)
    }

    private fun setNavigateTo(navigate: (String) -> Unit) {
        router.setNavigateTo(navigate)
    }

    data class ApolloTrackerAppState(
        val currentRoute: NavigationRoute = Splash,
        val isBackArrowVisible: Boolean = false
    )

    sealed interface Action {
        data class Navigate(val route: NavigationRoute): Action
        data class UpdateCurrentRoute(val route: String?): Action
        data class SetNavigateTo(val navigate: (String) -> Unit): Action
    }
}