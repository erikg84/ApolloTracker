package com.example.apollotracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.apollotracker.view.screen.AltcoinScreen
import com.example.apollotracker.view.screen.GraphScreen
import com.example.apollotracker.view.screen.MainScreen
import com.example.apollotracker.view.screen.SettingsScreen
import com.example.apollotracker.view.screen.SplashScreen

@Composable
fun ApolloTrackerNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Splash, modifier = modifier) {
        composable<Splash> {
            SplashScreen()
        }
        composable<Main> {
            MainScreen()
        }
        composable<Altcoin> {
            AltcoinScreen()
        }
        composable<Settings> {
            SettingsScreen()
        }
        composable<Graph> {
            GraphScreen()
        }
    }
}