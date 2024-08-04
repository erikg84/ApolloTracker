package com.example.apollotracker.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apollotracker.model.AltCoin
import com.example.apollotracker.view.components.ErrorComponent
import com.example.apollotracker.view.components.LoadingComponent
import com.example.apollotracker.viewmodel.AltcoinViewModel

@Composable
fun AltcoinScreen() {
    val viewModel: AltcoinViewModel = hiltViewModel()
    val viewState: AltcoinViewModel.ViewState by viewModel.viewState.collectAsState()
    when {
        viewState.isLoading -> LoadingComponent()
        viewState.isError -> ErrorComponent()
        else -> AltCoinInfo(viewState.altcoins, viewModel::onAction)
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.onAction(AltcoinViewModel.Action.StopRefresh) }
    }
}

@Composable
private fun AltCoinInfo(altcoinInfo: List<AltCoin>, onAction: (AltcoinViewModel.Action) -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(AltcoinViewModel.Action.GetAltcoin) }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(altcoinInfo) { altCoin ->
                    AltCoinItem(altCoin, onAction)
                }
            }
        }
    )
}

@Composable
private fun AltCoinItem(altCoin: AltCoin, onAction: (AltcoinViewModel.Action) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colors.primary)
        ) {
            with(altCoin) {
                Text(
                    text = "Name: $name",
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
                Text(
                    text = "Symbol: $symbol",
                    style = MaterialTheme.typography.body1,
                    color = Color.White
                )
                Text(
                    text = "Price: $$price",
                    style = MaterialTheme.typography.body1,
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { onAction(AltcoinViewModel.Action.ViewGraph(id)) }
                    ,
                    text = "View Graph",
                    style = MaterialTheme.typography.body1.copy(color = Color.Yellow)
                )
            }
        }
    }
}
