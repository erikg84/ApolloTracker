package com.example.apollotracker.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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
            FloatingActionButton(onClick = { onAction(AltcoinViewModel.Action.GetAltcoin) }, backgroundColor = Color.Blue) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
            ) {
                items(altcoinInfo) { altCoin ->
                    AltCoinItem(altCoin, onAction)
                    Divider(color = Color.LightGray, thickness = 1.dp)
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
            .padding(0.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            with(altCoin) {
                Text(
                    text = "Name: $name",
                    style = MaterialTheme.typography.h6,
                    color = Color.Black
                )
                Text(
                    text = "Symbol: $symbol",
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
                Text(
                    text = "Price: $$price",
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { onAction(AltcoinViewModel.Action.ViewGraph(id)) }
                    ,
                    text = "View Graph",
                    style = MaterialTheme.typography.body1.copy(color = Color.Blue)
                )
            }
        }
    }
}
