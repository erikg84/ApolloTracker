package com.example.apollotracker.viewmodel

import com.example.apollotracker.model.Currency
import com.example.apollotracker.util.SharedPreferencesManager
import com.example.apollotracker.testutil.TestCoroutineRule
import com.example.apollotracker.testutil.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val sharedPreferencesManager: SharedPreferencesManager = mockk(relaxed = true) {
        every { selectedCurrency } returns Currency.USD
    }

    private val viewModel: SettingsViewModel by lazy {
        SettingsViewModel(sharedPreferencesManager)
    }

    @Test
    fun `init should set initial state to selectedCurrency`() = runTest {
        val observer = viewModel.viewState.test(this)

        with(observer.changes.last()) {
            Assert.assertEquals(selectedCurrency, Currency.USD)
        }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<SettingsViewModel.ViewState>())
        }
    }

    @Test
    fun `onAction { ChangeCurrency } should update selectedCurrency`() = runTest {
        val newCurrency = Currency.GBP
        val observer = viewModel.viewState.test(this)

        viewModel.onAction(SettingsViewModel.Action.ChangeCurrency(newCurrency))

        with(observer.changes.last()) {
            Assert.assertEquals(selectedCurrency, newCurrency)
        }

        verify { sharedPreferencesManager.selectedCurrency = newCurrency }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<SettingsViewModel.ViewState>())
        }
    }
}
