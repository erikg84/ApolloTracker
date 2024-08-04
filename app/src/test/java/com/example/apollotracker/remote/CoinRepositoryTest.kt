package com.example.apollotracker.remote

import com.example.apollotracker.model.AltCoinResponseItem
import com.example.apollotracker.model.CoinPaprikaResponse
import com.example.apollotracker.model.HistoricalDataPoint
import com.example.apollotracker.testutil.TestCoroutineRule
import com.example.apollotracker.util.SharedPreferencesManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class CoinRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val coinPaprikaApi: CoinPaprikaApi = mockk(relaxed = true)
    private val sharedPreferencesManager: SharedPreferencesManager = mockk(relaxed = true)
    private val coinRepository: CoinRepository by lazy {
        CoinRepository(coinPaprikaApi, sharedPreferencesManager, testCoroutineRule.testDispatcher)
    }

    @Test
    fun `getBitcoinInfo should handle success`() = runTest {
        val response = CoinPaprikaResponse()
        coEvery { sharedPreferencesManager.selectedCurrency.code } returns "USD"
        coEvery { coinPaprikaApi.getBitcoinInfo("USD") } returns Response.success(response)

        val result = coinRepository.getBitcoinInfo()

        coVerify { coinPaprikaApi.getBitcoinInfo("USD") }
        Assert.assertEquals(CoinPaprikaResponse(), result.data)
    }

    @Test
    fun `getBitcoinInfo should handle failure`() = runTest {
        coEvery { sharedPreferencesManager.selectedCurrency.code } returns "USD"
        coEvery { coinPaprikaApi.getBitcoinInfo("USD") } returns Response.error(404, mockk(relaxed = true))

        val result = coinRepository.getBitcoinInfo()

        coVerify { coinPaprikaApi.getBitcoinInfo("USD") }
        Assert.assertTrue(result is Resource.Failure)
    }

    @Test
    fun `getBitcoinInfo should handle exception`() = runTest {
        coEvery { sharedPreferencesManager.selectedCurrency.code } returns "USD"
        coEvery { coinPaprikaApi.getBitcoinInfo("USD") } throws Exception("Network Error")

        val result = coinRepository.getBitcoinInfo()

        coVerify { coinPaprikaApi.getBitcoinInfo("USD") }
        Assert.assertTrue(result is Resource.Failure)
        Assert.assertEquals("Network Error", (result as Resource.Failure).error.message)
    }

    @Test
    fun `getAltcoinInfo should handle success`() = runTest {
        val response = listOf(AltCoinResponseItem())
        coEvery { sharedPreferencesManager.selectedCurrency.code } returns "USD"
        coEvery { coinPaprikaApi.getAltcoinInfo("USD") } returns Response.success(response)

        val result = coinRepository.getAltcoinInfo()

        coVerify { coinPaprikaApi.getAltcoinInfo("USD") }
        Assert.assertEquals(listOf(AltCoinResponseItem()), result.data)
    }

    @Test
    fun `getAltcoinInfo should handle failure`() = runTest {
        coEvery { sharedPreferencesManager.selectedCurrency.code } returns "USD"
        coEvery { coinPaprikaApi.getAltcoinInfo("USD") } returns Response.error(404, mockk(relaxed = true))

        val result = coinRepository.getAltcoinInfo()

        coVerify { coinPaprikaApi.getAltcoinInfo("USD") }
        Assert.assertTrue(result is Resource.Failure)
    }

    @Test
    fun `getAltcoinInfo should handle exception`() = runTest {
        coEvery { sharedPreferencesManager.selectedCurrency.code } returns "USD"
        coEvery { coinPaprikaApi.getAltcoinInfo("USD") } throws Exception("Network Error")

        val result = coinRepository.getAltcoinInfo()

        coVerify { coinPaprikaApi.getAltcoinInfo("USD") }
        Assert.assertTrue(result is Resource.Failure)
        Assert.assertEquals("Network Error", (result as Resource.Failure).error.message)
    }

    @Test
    fun `getHistoricalInfo should handle success`() = runTest {
        val coinId = "bitcoin"
        val startDate = "2023-08-06"
        val interval = "1d"
        val response = listOf(HistoricalDataPoint())
        coEvery { coinPaprikaApi.getHistoricalInfo(coinId, startDate, interval) } returns Response.success(response)

        val result = coinRepository.getHistoricalInfo(coinId, startDate, interval)

        coVerify { coinPaprikaApi.getHistoricalInfo(coinId, startDate, interval) }
        Assert.assertEquals(listOf(HistoricalDataPoint()), result.data)
    }

    @Test
    fun `getHistoricalInfo should handle failure`() = runTest {
        val coinId = "bitcoin"
        val startDate = "2023-08-06"
        val interval = "1d"
        coEvery { coinPaprikaApi.getHistoricalInfo(coinId, startDate, interval) } returns Response.error(404, mockk(relaxed = true))

        val result = coinRepository.getHistoricalInfo(coinId, startDate, interval)

        coVerify { coinPaprikaApi.getHistoricalInfo(coinId, startDate, interval) }
        Assert.assertTrue(result is Resource.Failure)
    }

    @Test
    fun `getHistoricalInfo should handle exception`() = runTest {
        val coinId = "bitcoin"
        val startDate = "2023-08-06"
        val interval = "1d"
        coEvery { coinPaprikaApi.getHistoricalInfo(coinId, startDate, interval) } throws Exception("Network Error")

        val result = coinRepository.getHistoricalInfo(coinId, startDate, interval)

        coVerify { coinPaprikaApi.getHistoricalInfo(coinId, startDate, interval) }
        Assert.assertTrue(result is Resource.Failure)
        Assert.assertEquals("Network Error", (result as Resource.Failure).error.message)
    }

    @Test
    fun `setCurrentCoinId should update currentCoinId`() {
        val coinId = "bitcoin"

        coinRepository.setCurrentCoinId(coinId)

        Assert.assertEquals(coinId, coinRepository.currentCoinId.value)
    }
}
