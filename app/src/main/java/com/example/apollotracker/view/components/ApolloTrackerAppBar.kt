package com.example.apollotracker.view.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

@Composable
fun ApolloTrackerAppBar(
    isBackArrowVisible: Boolean,
    popBackStack: () -> Unit,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Apollo Tracker") },
        navigationIcon = {
            IconButton(onClick = onMenuClick.takeUnless { isBackArrowVisible } ?: popBackStack) {
                Icon(
                    imageVector = Icons.Default.Menu.takeUnless { isBackArrowVisible } ?: Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Menu"
                )
            }
        }
    )
}