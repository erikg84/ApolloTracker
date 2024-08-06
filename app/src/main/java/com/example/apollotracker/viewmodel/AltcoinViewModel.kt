package com.example.apollotracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollotracker.model.AltCoin
import com.example.apollotracker.model.AltCoinResponseItem
import com.example.apollotracker.navigation.Graph
import com.example.apollotracker.navigation.Router
import com.example.apollotracker.remote.CoinRepository
import com.example.apollotracker.remote.Resource
import com.example.apollotracker.store.ModelStore
import com.example.apollotracker.store.StatefulStore
import com.example.apollotracker.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AltcoinViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val router: Router
) : ViewModel() {

    private val statefulStore: ModelStore<ViewState> = StatefulStore(ViewState(), viewModelScope)
    val viewState = statefulStore.state

    init {
        pollAltcoinInfo()
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.GetAltcoin -> getAltcoinInfo()
            is Action.StopRefresh -> stopAutoRefresh()
            is Action.ViewGraph -> navigateToGraph(action.coinId)
        }
    }

    private fun pollAltcoinInfo() {
        setLoadingState(true)
        viewModelScope.launch {
            coinRepository.startAltcoinPolling(REFRESH_INTERVAL).collect { resource ->
                setLoadingState(true)
                handleResource(resource)
            }
        }
    }

    private fun getAltcoinInfo() {
        setLoadingState(true)
        viewModelScope.launch {
            handleResource(coinRepository.getAltcoinInfo())
        }
    }

    private fun handleResource(result: Resource<List<AltCoinResponseItem>>) {
        when (result) {
            is Resource.Success -> handleSuccess(result.data)
            is Resource.Failure -> handleError(result.error)
            else -> Unit
        }
    }

    private fun handleError(error: Throwable) {
        Log.e(TAG, "Error: $error")
        statefulStore.process { oldState -> oldState.copy(isLoading = false, isError = true) }
    }

    private fun handleSuccess(altcoinInfo: List<AltCoinResponseItem>) {
        statefulStore.process { oldState ->
            oldState.copy(
                isLoading = false,
                altcoins = altcoinInfo.map {
                    AltCoin(
                        id = it.id,
                        name = it.name,
                        symbol = it.symbol,
                        price = it.quotes?.getCurrency(sharedPreferencesManager.selectedCurrency)?.price
                    )
                }
            )
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        statefulStore.process { it.copy(isLoading = isLoading) }
    }

    private fun navigateToGraph(coinId: String?) {
        coinRepository.setCurrentCoinId(coinId.orEmpty())
        router.navigateTo(Graph)
    }

    private fun stopAutoRefresh() {
        coinRepository.stopBitcoinPolling()
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val altcoins: List<AltCoin> = emptyList()
    )

    sealed interface Action {
        data object GetAltcoin : Action
        data object StopRefresh : Action
        data class ViewGraph(val coinId: String?) : Action
    }

    companion object {
        private val TAG = AltcoinViewModel::class.java.simpleName
        private const val REFRESH_INTERVAL = 60000L
        private const val DELAY = 0L
    }
}