package com.example.apollotracker.navigation

import com.example.apollotracker.testutil.TestCoroutineRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RouterTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val router = Router()

    @Test
    fun `setNavigateTo should set navigateTo lambda`() {
        val navigateMock: (String) -> Unit = mockk(relaxed = true)

        router.setNavigateTo(navigateMock)

        router.navigateTo(Splash)

        verify { navigateMock.invoke(Splash.route) }
    }

    @Test
    fun `updateCurrentRoute should update current route`() = runTest {
        router.updateCurrentRoute(Main)

        val currentRoute = router.currentRoute.first()

        Assert.assertEquals(Main, currentRoute)
    }

    @Test
    fun `navigateTo should invoke navigateTo lambda`() {
        val navigateMock: (String) -> Unit = mockk(relaxed = true)

        router.setNavigateTo(navigateMock)
        router.navigateTo(Main)

        verify { navigateMock.invoke(Main.route) }
    }

    @Test
    fun `navigateTo should not throw when navigateTo lambda is not set`() {
        try {
            router.navigateTo(Main)
        } catch (e: Exception) {
            Assert.fail("navigateTo should not throw an exception when navigateTo lambda is not set")
        }
    }
}
