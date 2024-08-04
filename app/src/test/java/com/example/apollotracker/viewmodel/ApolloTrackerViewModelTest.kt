package com.example.apollotracker.viewmodel

import com.example.apollotracker.navigation.Main
import com.example.apollotracker.navigation.NavigationRoute
import com.example.apollotracker.navigation.Router
import com.example.apollotracker.navigation.Splash
import com.example.apollotracker.testutil.TestCoroutineRule
import com.example.apollotracker.testutil.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ApolloTrackerViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val router: Router = mockk(relaxed = true) {
        every { currentRoute } returns MutableStateFlow(Splash)
    }
    private val viewModel: ApolloTrackerViewModel by lazy {
        ApolloTrackerViewModel(router)
    }

    @Before
    fun setup() {
        coEvery { router.currentRoute } returns MutableStateFlow(Splash)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `init should set initial route to Splash`() = runTest {
        val observer = viewModel.appState.test(this)

        with(observer.changes.last()) {
            Assert.assertEquals(Splash, currentRoute)
            Assert.assertFalse(isBackArrowVisible)
        }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<ApolloTrackerViewModel.ApolloTrackerAppState>())
        }
    }

    @Test
    fun `navigate to a route updates current route`() = runTest {
        val observer = viewModel.appState.test(this)
        val route = Main

        viewModel.onAction(ApolloTrackerViewModel.Action.Navigate(route))

        coVerify { router.navigateTo(route) }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<ApolloTrackerViewModel.ApolloTrackerAppState>())
        }
    }

    @Test
    fun `update current route updates state and back arrow visibility`() = runTest {
        val observer = viewModel.appState.test(this)
        val route = Main.route

        viewModel.onAction(ApolloTrackerViewModel.Action.UpdateCurrentRoute(route))

        with(observer.changes.first()) {
            Assert.assertFalse(isBackArrowVisible)
        }

        coVerify { router.updateCurrentRoute(Main) }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<ApolloTrackerViewModel.ApolloTrackerAppState>())
        }
    }

    @Test
    fun `update current route to non-main updates state and sets back arrow visible`() = runTest {
        val observer = viewModel.appState.test(this)
        val route = "someOtherRoute"

        viewModel.onAction(ApolloTrackerViewModel.Action.UpdateCurrentRoute(route))

        with(observer.changes.last()) {
            Assert.assertTrue(isBackArrowVisible)
        }

        coVerify { router.updateCurrentRoute(NavigationRoute.fromString(route)) }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<ApolloTrackerViewModel.ApolloTrackerAppState>())
        }
    }

    @Test
    fun `set navigate to function updates router`() = runTest {
        val navigate: (String) -> Unit = {}

        viewModel.onAction(ApolloTrackerViewModel.Action.SetNavigateTo(navigate))

        coVerify { router.setNavigateTo(navigate) }
    }
}
