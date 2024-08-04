package com.example.apollotracker.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apollotracker.navigation.Altcoin
import com.example.apollotracker.navigation.Main
import com.example.apollotracker.navigation.Settings
import com.example.apollotracker.viewmodel.ApolloTrackerViewModel
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(onAction: (ApolloTrackerViewModel.Action) -> Unit, closeDrawer: suspend () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Apollo Tracker", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {
            onAction(ApolloTrackerViewModel.Action.Navigate(Main))
            coroutineScope.launch { closeDrawer() }
        }) {
            Text(text = "Main")
        }
        TextButton(onClick = {
            onAction(ApolloTrackerViewModel.Action.Navigate(Altcoin))
            coroutineScope.launch { closeDrawer() }
        }) {
            Text(text = "Altcoin")
        }
        TextButton(onClick = {
            onAction(ApolloTrackerViewModel.Action.Navigate(Settings))
            coroutineScope.launch { closeDrawer() }
        }) {
            Text(text = "Settings")
        }
    }
}