package com.example.apollotracker.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.apollotracker.navigation.ApolloTrackerNavHost
import com.example.apollotracker.view.components.ApolloTrackerAppBar
import com.example.apollotracker.view.components.ApolloTrackerBottomBar
import com.example.apollotracker.view.components.DrawerContent
import com.example.apollotracker.viewmodel.ApolloTrackerViewModel
import kotlinx.coroutines.launch

@Composable
fun ApolloTracker() {
    val viewModel: ApolloTrackerViewModel = viewModel()
    val appState by viewModel.appState.collectAsState()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(viewModel::onAction, drawerState::close)
        }
    ) {
        Scaffold(
            topBar = {
                if (!appState.currentRoute.isSplash) {
                    ApolloTrackerAppBar(isBackArrowVisible = appState.isBackArrowVisible, popBackStack = navController::popBackStack) {
                        coroutineScope.launch { drawerState.open() }
                    }
                }
            },
            bottomBar = {
                if (!appState.currentRoute.isSplash) {
                    ApolloTrackerBottomBar(viewModel::onAction)
                }
            }
        ) { paddingValues ->
            ApolloTrackerNavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onAction(ApolloTrackerViewModel.Action.SetNavigateTo(navController::navigate))
    }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.onAction(ApolloTrackerViewModel.Action.UpdateCurrentRoute(destination.route))
        }
    }
}