package com.example.apollotracker.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.apollotracker.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcePollingManager @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val connectivityManager: ConnectivityManager
) {

    private val _pollingState = MutableStateFlow(true)

    fun setPollingState(isActive: Boolean) {
        _pollingState.value = isActive
    }

    @ExperimentalCoroutinesApi
    fun <T> pollDataSource(delayMillis: Long = DEFAULT_POLLING_INTERVAL, fetchData: suspend () -> T): Flow<T> = channelFlow {
        try {
            _pollingState.collect { isPollingActive ->
                if (!isPollingActive) {
                    close()
                    return@collect
                }

                if (!isNetworkAvailable()) {
                    close()
                    return@collect
                }

                while (isActive) {
                    val data = withContext(dispatcher) { fetchData() }
                    send(data)
                    delay(delayMillis)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while polling data", e)
            close(e)
        }
    }.flowOn(dispatcher)

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    companion object {
        private const val DEFAULT_POLLING_INTERVAL: Long = 6000L
        private val TAG = ResourcePollingManager::class.java.simpleName
    }
}