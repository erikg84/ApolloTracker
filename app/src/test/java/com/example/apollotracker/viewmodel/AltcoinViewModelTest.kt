package com.example.apollotracker.viewmodel

import android.util.Log
import com.example.apollotracker.model.AltCoin
import com.example.apollotracker.model.AltCoinResponseItem
import com.example.apollotracker.model.CoinCurrency
import com.example.apollotracker.model.Currency
import com.example.apollotracker.model.Quotes
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AltcoinViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val coinRepository: CoinRepository = mockk(relaxed = true)
    private val sharedPreferencesManager: SharedPreferencesManager = mockk(relaxed = true) {
        every { selectedCurrency } returns Currency.USD
    }
    private val router: Router = mockk(relaxed = true)
    private val viewModel: AltcoinViewModel by lazy {
        AltcoinViewModel(coinRepository, sharedPreferencesManager, router)
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
    fun `init should call getAltcoinInfo and handle success`() = runTest {
        val altcoinResponseItem = AltCoinResponseItem(id = "eth-ethereum", name = "Ethereum", symbol = "ETH", quotes = Quotes(uSD = CoinCurrency(price = 4000.0F)))
        val altcoinResponse = listOf(altcoinResponseItem)

        coEvery { coinRepository.startAltcoinPolling(any()) } returns flowOf(Resource.Success(altcoinResponse))

        val observer = viewModel.viewState.test(this)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertFalse(isError)
            Assert.assertEquals(altcoins, listOf(AltCoin(id = "eth-ethereum", name = "Ethereum", symbol = "ETH", price = 4000.0F)))
        }

        coVerify { coinRepository.startAltcoinPolling(any()) }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<AltcoinViewModel.ViewState>())
        }
    }

    @Test
    fun `init should call getAltcoinInfo and should handle error response`() = runTest {
        val error = Throwable("Network Error")

        coEvery { coinRepository.startAltcoinPolling(any()) } returns flowOf(Resource.Failure(error = error))

        val observer = viewModel.viewState.test(this)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertTrue(isError)
            Assert.assertEquals(altcoins, emptyList<AltCoin>())
        }

        coVerify { coinRepository.startAltcoinPolling(any()) }
        verify { Log.e(TAG, "Error: $error") }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<AltcoinViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { getAltcoinInfo action } should handle success response`() = runTest {
        val altcoinResponseItem = AltCoinResponseItem(id = "eth-ethereum", name = "Ethereum", symbol = "ETH", quotes = Quotes(uSD = CoinCurrency(price = 4000.0F)))
        val altcoinResponse = listOf(altcoinResponseItem)

        coEvery { coinRepository.getAltcoinInfo() } returns Resource.Success(altcoinResponse)

        val observer = viewModel.viewState.test(this)

        viewModel.onAction(AltcoinViewModel.Action.GetAltcoin)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertFalse(isError)
            Assert.assertEquals(altcoins, listOf(AltCoin(id = "eth-ethereum", name = "Ethereum", symbol = "ETH", price = 4000.0F)))
        }

        coVerify { coinRepository.getAltcoinInfo() }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<AltcoinViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { getAltcoinInfo action } should call getAltcoinInfo and should handle error response`() = runTest {
        val error = Throwable("Network Error")

        coEvery { coinRepository.getAltcoinInfo() } returns Resource.Failure(error = error)

        val observer = viewModel.viewState.test(this)

        viewModel.onAction(AltcoinViewModel.Action.GetAltcoin)

        with(observer.changes.last()) {
            Assert.assertFalse(isLoading)
            Assert.assertTrue(isError)
            Assert.assertEquals(altcoins, emptyList<AltCoin>())
        }

        coVerify { coinRepository.getAltcoinInfo() }
        verify { Log.e(TAG, "Error: $error") }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<AltcoinViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { view graph action } should navigate to graph`() = runTest {
        val altcoinResponseItem = AltCoinResponseItem(id = "eth-ethereum")
        val altcoinResponse = listOf(altcoinResponseItem)

        coEvery { coinRepository.startAltcoinPolling(any()) } returns flowOf(Resource.Success(altcoinResponse))

        val observer = viewModel.viewState.test(this)

        viewModel.onAction(AltcoinViewModel.Action.ViewGraph("eth-ethereum"))

        verify { router.navigateTo(Graph) }
        coVerify { coinRepository.setCurrentCoinId("eth-ethereum") }
        coVerify { coinRepository.startAltcoinPolling(any()) }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<AltcoinViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { stop refresh action } should stop auto refresh`() = runTest {
        coEvery { coinRepository.getAltcoinInfo() } returns Resource.Success(emptyList())

        val observer = viewModel.viewState.test(this)

        viewModel.onAction(AltcoinViewModel.Action.StopRefresh)

        verify { coinRepository.stopBitcoinPolling() }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<AltcoinViewModel.ViewState>())
        }
    }

    companion object {
        private val TAG = AltcoinViewModel::class.java.simpleName
    }
}
