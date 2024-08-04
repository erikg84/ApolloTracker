package com.example.apollotracker.viewmodel

import android.util.Log
import com.example.apollotracker.model.CoinPaprikaResponse
import com.example.apollotracker.model.Currency
import com.example.apollotracker.model.Quote
import com.example.apollotracker.navigation.Graph
import com.example.apollotracker.navigation.Router
import com.example.apollotracker.remote.CoinRepository
import com.example.apollotracker.remote.Resource
import com.example.apollotracker.util.SharedPreferencesManager
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
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Timer

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val coinRepository: CoinRepository = mockk(relaxed = true)
    private val sharedPreferencesManager: SharedPreferencesManager = mockk(relaxed = true) {
        every { selectedCurrency } returns Currency.USD
    }
    private val router: Router = mockk(relaxed = true)
    private val viewModel: MainViewModel by lazy {
        MainViewModel(coinRepository, sharedPreferencesManager, router)
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
    fun `init should call getBitcoinInfo and handle success`() = runTest {
        val bitcoin = CoinPaprikaResponse(id = "btc-bitcoin", name = "Bitcoin", quotes = mapOf("USD" to Quote(price = 60000.0)))

        coEvery { coinRepository.getBitcoinInfo() } returns Resource.Success(bitcoin)

        val observer = viewModel.viewState.test(this)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertFalse(isError)
            Assert.assertEquals(bitcoinInfo, bitcoin)
            Assert.assertEquals(quote, Quote(price = 60000.0))
        }

        coVerify { coinRepository.getBitcoinInfo() }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<MainViewModel.ViewState>())
        }
    }

    @Test
    fun `init should call getBitcoinInfo and should handle error response`() = runTest {
        val error = Throwable("Network Error")

        coEvery { coinRepository.getBitcoinInfo() } returns Resource.Failure(error = error)

        val observer = viewModel.viewState.test(this)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertTrue(isError)
            Assert.assertEquals(bitcoinInfo, CoinPaprikaResponse())
            Assert.assertNull(quote)
        }

        coVerify { coinRepository.getBitcoinInfo() }
        verify { Log.e(TAG, "Error: $error") }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<MainViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { getBitcoinInfo action } should handle success response`() = runTest {
        val bitcoin = CoinPaprikaResponse(id = "btc-bitcoin", name = "Bitcoin", quotes = mapOf("USD" to Quote(price = 60000.0)))

        coEvery { coinRepository.getBitcoinInfo() } returns Resource.Success(bitcoin)

        val observer = viewModel.viewState.test(this)

        viewModel.onAction(MainViewModel.Action.GetBitCoin)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertFalse(isError)
            Assert.assertEquals(bitcoinInfo, bitcoin)
            Assert.assertEquals(quote, Quote(price = 60000.0))
        }

        coVerify { coinRepository.getBitcoinInfo() }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<MainViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { getBitcoinInfo action } should call getBitcoinInfo and should handle error response`() = runTest {
        val error = Throwable("Network Error")

        coEvery { coinRepository.getBitcoinInfo() } returns Resource.Failure(error = error)

        val observer = viewModel.viewState.test(this)

        viewModel.onAction(MainViewModel.Action.GetBitCoin)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertTrue(isError)
            Assert.assertEquals(bitcoinInfo, CoinPaprikaResponse())
            Assert.assertNull(quote)
        }

        coVerify { coinRepository.getBitcoinInfo() }
        verify { Log.e(TAG, "Error: $error") }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<MainViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { view graph action } should navigate to graph`() = runTest {
        val bitcoin = CoinPaprikaResponse(id = "btc-bitcoin")

        coEvery { coinRepository.getBitcoinInfo() } returns Resource.Success(bitcoin)

        val observer = viewModel.viewState.test(this)

        viewModel.onAction(MainViewModel.Action.ViewGraph)

        verify { router.navigateTo(Graph) }
        coVerify { coinRepository.setCurrentCoinId("btc-bitcoin") }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<MainViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { stop refresh action } should stop auto refresh`() = runTest {

        coEvery { coinRepository.getBitcoinInfo() } returns Resource.Success(CoinPaprikaResponse())

        val observer = viewModel.viewState.test(this)

        val timer: Timer = mockk(relaxed = true)
        viewModel.timer = timer

        viewModel.onAction(MainViewModel.Action.StopRefresh)

        verify { timer.cancel() }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<MainViewModel.ViewState>())
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}