package com.example.apollotracker.util

import android.content.Context
import com.example.apollotracker.model.Currency
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var selectedCurrency: Currency
        get() {
            val currencyCode = sharedPreferences.getString(KEY_CURRENCY, Currency.USD.code) ?: Currency.USD.code
            return Currency.valueOf(currencyCode)
        }
        set(value) {
            sharedPreferences.edit().putString(KEY_CURRENCY, value.code).apply()
        }

    companion object {
        private const val PREFS_NAME = "apollo_tracker_prefs"
        private const val KEY_CURRENCY = "selected_currency"
    }
}
