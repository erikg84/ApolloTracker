package com.example.apollotracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollotracker.model.HistoricalDataPoint
import com.example.apollotracker.remote.CoinRepository
import com.example.apollotracker.remote.Resource
import com.example.apollotracker.store.ModelStore
import com.example.apollotracker.store.StatefulStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val coinRepository: CoinRepository
): ViewModel() {

    private val statefulStore: ModelStore<ViewState> = StatefulStore(ViewState(), viewModelScope)
    val viewState = statefulStore.state

    init {
        viewModelScope.launch {
            coinRepository.currentCoinId.collect { coinId ->
                getHistoricalInfo(coinId)
            }
        }
    }

    private fun getHistoricalInfo(coinId: String) {
        setLoadingState(true)
        viewModelScope.launch {
            when (val result = coinRepository.getHistoricalInfo(coinId)) {
                is Resource.Success -> handleSuccess(result.data)
                is Resource.Failure -> handleError(result.error)
                else -> Unit
            }
        }
    }

    private fun handleSuccess(historicalDataPoints: List<HistoricalDataPoint>) {
        statefulStore.process { oldState -> oldState.copy(isLoading = false, historicalDataPoints = historicalDataPoints) }
    }

    private fun setLoadingState(isLoading: Boolean) {
        statefulStore.process { oldState -> oldState.copy(isLoading = isLoading) }
    }

    private fun handleError(error: Throwable) {
        Log.e(TAG, "Error: $error")
        statefulStore.process { oldState -> oldState.copy(isLoading = false, isError = true) }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val historicalDataPoints: List<HistoricalDataPoint> = emptyList()
    )

    companion object {
        private val TAG = GraphViewModel::class.java.simpleName
    }
}