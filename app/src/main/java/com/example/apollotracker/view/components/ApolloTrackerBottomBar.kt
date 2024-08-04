package com.example.apollotracker.view.components

import androidx.compose.material.BottomAppBar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.apollotracker.navigation.Altcoin
import com.example.apollotracker.navigation.Main
import com.example.apollotracker.navigation.Settings
import com.example.apollotracker.viewmodel.ApolloTrackerViewModel

@Composable
fun ApolloTrackerBottomBar(
    onAction: (ApolloTrackerViewModel.Action) -> Unit
) {
    BottomAppBar {
        TextButton(
            onClick = { onAction(ApolloTrackerViewModel.Action.Navigate(Main)) },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Main", color = Color.White)
        }
        TextButton(
            onClick = { onAction(ApolloTrackerViewModel.Action.Navigate(Altcoin)) },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Altcoin", color = Color.White)
        }
        TextButton(
            onClick = { onAction(ApolloTrackerViewModel.Action.Navigate(Settings)) },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Settings", color = Color.White)
        }
    }
}