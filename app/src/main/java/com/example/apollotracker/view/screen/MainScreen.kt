package com.example.apollotracker.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
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
import com.example.apollotracker.model.CoinPaprikaResponse
import com.example.apollotracker.model.Quote
import com.example.apollotracker.view.components.ErrorComponent
import com.example.apollotracker.view.components.LoadingComponent
import com.example.apollotracker.viewmodel.MainViewModel

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val viewState: MainViewModel.ViewState by viewModel.viewState.collectAsState()
    when {
        viewState.isLoading -> LoadingComponent()
        viewState.isError -> ErrorComponent()
        else -> BitcoinInfo(viewState.bitcoinInfo, viewState.quote, viewModel::onAction)
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.onAction(MainViewModel.Action.StopRefresh) }
    }
}

@Composable
private fun BitcoinInfo(
    bitcoinInfo: CoinPaprikaResponse,
    quote: Quote?,
    onAction: (MainViewModel.Action) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(MainViewModel.Action.GetBitCoin) }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                with(bitcoinInfo) {
                    Text(text = "Name: $name", style = MaterialTheme.typography.h6)
                    Text(text = "Symbol: $symbol", style = MaterialTheme.typography.body1)
                    Text(text = "Last Updated: $lastUpdated", style = MaterialTheme.typography.body1)
                }
                Divider()
                quote?.run {
                    Text(text = "Price: $$price", style = MaterialTheme.typography.h6)
                }
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { onAction(MainViewModel.Action.ViewGraph) },
                    text = "View Graph",
                    style = MaterialTheme.typography.body1.copy(color = Color.Blue)
                )
            }
        }
    )
}
