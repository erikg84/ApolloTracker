package com.example.apollotracker.remote

import com.example.apollotracker.di.IODispatcher
import com.example.apollotracker.model.AltCoinResponseItem
import com.example.apollotracker.model.CoinPaprikaResponse
import com.example.apollotracker.model.HistoricalDataPoint
import com.example.apollotracker.remote.Resource.Companion.fetchCatching
import com.example.apollotracker.util.SharedPreferencesManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepository @Inject constructor(
    private val coinPaprikaApi: CoinPaprikaApi,
    private val sharedPreferencesManager: SharedPreferencesManager,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val currency: String get() = sharedPreferencesManager.selectedCurrency.code
    private val _currentCoinId = MutableStateFlow("")
    val currentCoinId = _currentCoinId.asStateFlow()

    suspend fun getBitcoinInfo(): Resource<CoinPaprikaResponse> {
        return withContext(ioDispatcher) {
            fetchCatching { coinPaprikaApi.getBitcoinInfo(currency) }
        }
    }

    suspend fun getAltcoinInfo(): Resource<List<AltCoinResponseItem>> {
        return withContext(ioDispatcher) {
            fetchCatching { coinPaprikaApi.getAltcoinInfo(currency) }
        }
    }

    suspend fun getHistoricalInfo(coinId: String, startDate: String = "2024-01-02", interval: String = "1d"): Resource<List<HistoricalDataPoint>> {
        return withContext(ioDispatcher) {
            fetchCatching { coinPaprikaApi.getHistoricalInfo(coinId, startDate, interval) }
        }
    }

    fun setCurrentCoinId(coinId: String) {
        _currentCoinId.value = coinId
    }
}