package com.example.apollotracker.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apollotracker.navigation.ApolloTrackerNavHost
import com.example.apollotracker.navigation.Back
import com.example.apollotracker.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ApolloTrackerNavHost(navController)
            setupNavigation(navController)
        }
    }

    private fun setupNavigation(navController: NavHostController) {
        lifecycleScope.launch {
            viewModel.currentRoute.collect { route ->
                when (route) {
                    is Back -> navController.popBackStack()
                    else -> navController.navigate(route)
                }
            }
        }
    }
}