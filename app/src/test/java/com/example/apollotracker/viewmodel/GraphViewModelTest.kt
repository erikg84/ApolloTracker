package com.example.apollotracker.viewmodel

import android.util.Log
import com.example.apollotracker.model.HistoricalDataPoint
import com.example.apollotracker.remote.CoinRepository
import com.example.apollotracker.remote.Resource
import com.example.apollotracker.testutil.TestCoroutineRule
import com.example.apollotracker.testutil.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GraphViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val coinRepository: CoinRepository = mockk(relaxed = true) {
        every { currentCoinId } returns MutableStateFlow("btc-bitcoin")
    }
    private val viewModel: GraphViewModel by lazy {
        GraphViewModel(coinRepository)
    }

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `init should call getHistoricalInfo and handle success`() = runTest {
        val historicalData = listOf(HistoricalDataPoint(price = 60000f, timestamp = 1627689600.toString()))

        coEvery { coinRepository.getHistoricalInfo("btc-bitcoin") } returns Resource.Success(historicalData)

        val observer = viewModel.viewState.test(this)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertFalse(isError)
            Assert.assertEquals(historicalDataPoints, historicalData)
        }

        coVerify { coinRepository.getHistoricalInfo("btc-bitcoin") }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<GraphViewModel.ViewState>())
        }
    }

    @Test
    fun `init should call getHistoricalInfo and handle error`() = runTest {
        val error = Throwable("Network Error")

        coEvery { coinRepository.getHistoricalInfo("btc-bitcoin") } returns Resource.Failure(error = error)

        val observer = viewModel.viewState.test(this)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertTrue(isError)
            Assert.assertEquals(historicalDataPoints, emptyList<HistoricalDataPoint>())
        }

        coVerify { coinRepository.getHistoricalInfo("btc-bitcoin") }
        verify { Log.e(TAG, "Error: $error") }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<GraphViewModel.ViewState>())
        }
    }

    companion object {
        private const val TAG = "GraphViewModel"
    }
}
