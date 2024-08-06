package com.example.apollotracker.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import com.example.apollotracker.testutil.TestCoroutineRule
import com.example.apollotracker.testutil.test
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*

@ExperimentalCoroutinesApi
class ResourcePollingManagerTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)
    private val resourcePollingManager: ResourcePollingManager by lazy {
        ResourcePollingManager(testCoroutineRule.testDispatcher, connectivityManager)
    }

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `pollDataSource should emit data when network is available and polling is active`() = runTest {
        val fetchData: suspend () -> String = { "TestData" }
        val network: Network = mockk()
        val networkCapabilities: NetworkCapabilities = mockk()

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        val flow = resourcePollingManager.pollDataSource(delayMillis = 1000L, fetchData = fetchData).test(this)

        advanceTimeBy(3000L)

        Assert.assertEquals(listOf("TestData", "TestData", "TestData"), flow.changes)

        flow.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<String>())
        }
    }

    @Test
    fun `pollDataSource should not emit data when network is unavailable`() = runTest {
        val fetchData: suspend () -> String = { "TestData" }

        every { connectivityManager.activeNetwork } returns null

        val flow = resourcePollingManager.pollDataSource(delayMillis = 1000L, fetchData = fetchData).test(this)

        Assert.assertTrue(flow.changes.isEmpty())

        flow.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<String>())
        }
    }

    @Test
    fun `pollDataSource should not emit data when polling is inactive`() = runTest {
        val fetchData: suspend () -> String = { "TestData" }
        val network: Network = mockk()
        val networkCapabilities: NetworkCapabilities = mockk()

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        resourcePollingManager.setPollingState(false)
        val flow = resourcePollingManager.pollDataSource(delayMillis = 1000L, fetchData = fetchData).test(this)

        Assert.assertTrue(flow.changes.isEmpty())

        flow.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<String>())
        }
    }

    @Test
    fun `pollDataSource should handle exceptions and log errors`() = runTest {
        val fetchData: suspend () -> String = { throw Exception("Fetch error") }
        val network: Network = mockk()
        val networkCapabilities: NetworkCapabilities = mockk()

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        val flow = resourcePollingManager.pollDataSource(delayMillis = 1000L, fetchData = fetchData)

        flow.catch { e ->
            Assert.assertEquals("Fetch error", e.message)
        }.collect()
    }
}