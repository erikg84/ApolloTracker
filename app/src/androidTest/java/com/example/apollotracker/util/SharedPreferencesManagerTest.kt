package com.example.apollotracker.util

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.apollotracker.model.Currency
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
class SharedPreferencesManagerTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    @Before
    fun setup() {
        hiltRule.inject()
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = mockk(relaxed = true)
        every { context.getSharedPreferences(any(), any()) } returns sharedPreferences

        sharedPreferencesManager = SharedPreferencesManager(context)
    }

    @Test
    fun selectedCurrency_default_value_should_be_USD() {
        val currencySlot = slot<String>()
        every { sharedPreferences.getString(any(), capture(currencySlot)) } returns Currency.USD.code

        val selectedCurrency = sharedPreferencesManager.selectedCurrency

        assertEquals(Currency.USD, selectedCurrency)
        assertEquals(KEY_CURRENCY, currencySlot.captured)
    }

    @Test
    fun selectedCurrency_should_return_stored_value() {
        every { sharedPreferences.getString(KEY_CURRENCY, Currency.USD.code) } returns Currency.GBP.code

        val selectedCurrency = sharedPreferencesManager.selectedCurrency

        assertEquals(Currency.GBP, selectedCurrency)
    }

    @Test
    fun selectedCurrency_should_save_value_to_shared_preferences() {
        val editor = mockk<SharedPreferences.Editor>(relaxed = true)
        every { sharedPreferences.edit() } returns editor

        sharedPreferencesManager.selectedCurrency = Currency.EUR

        verify { editor.putString(KEY_CURRENCY, Currency.EUR.code) }
        verify { editor.apply() }
    }

    companion object {
        private const val KEY_CURRENCY = "selected_currency"
    }
}
