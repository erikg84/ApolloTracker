package com.example.apollotracker.view.components

import androidx.compose.material.BottomAppBar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.example.apollotracker.navigation.Altcoin
import com.example.apollotracker.navigation.Main
import com.example.apollotracker.navigation.Settings
import com.example.apollotracker.viewmodel.ApolloTrackerViewModel

@Composable
fun ApolloTrackerBottomBar(
    onAction: (ApolloTrackerViewModel.Action) -> Unit
) {
    BottomAppBar(
        modifier = Modifier.testTag("BottomNavBar"),
        backgroundColor = Color(0xFF121212),
        contentColor = Color.White
    ) {
        TextButton(
            onClick = { onAction(ApolloTrackerViewModel.Action.Navigate(Main)) },
            modifier = Modifier.weight(1f).testTag("MainDestinationButton")
        ) {
            Text(text = "Main", color = Color.White)
        }
        TextButton(
            onClick = { onAction(ApolloTrackerViewModel.Action.Navigate(Altcoin)) },
            modifier = Modifier.weight(1f).testTag("AltcoinDestinationButton")
        ) {
            Text(text = "Altcoin", color = Color.White)
        }
        TextButton(
            onClick = { onAction(ApolloTrackerViewModel.Action.Navigate(Settings)) },
            modifier = Modifier.weight(1f).testTag("SettingsDestinationButton")
        ) {
            Text(text = "Settings", color = Color.White)
        }
    }
}