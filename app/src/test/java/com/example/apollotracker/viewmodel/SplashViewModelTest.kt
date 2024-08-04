package com.example.apollotracker.viewmodel

import com.example.apollotracker.navigation.Main
import com.example.apollotracker.navigation.Router
import com.example.apollotracker.testutil.TestCoroutineRule
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val router: Router = mockk(relaxed = true)
    private val viewModel: SplashViewModel by lazy {
        SplashViewModel(router)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `start timer navigates to main after delay`() = runTest {
        viewModel.onAction(SplashViewModel.Action.StartTimer)

        advanceUntilIdle()

        coVerify { router.navigateTo(Main) }
    }
}
