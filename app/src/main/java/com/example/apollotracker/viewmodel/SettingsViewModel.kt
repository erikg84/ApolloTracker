package com.example.apollotracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollotracker.model.Currency
import com.example.apollotracker.store.ModelStore
import com.example.apollotracker.store.StatefulStore
import com.example.apollotracker.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val statefulStore: ModelStore<ViewState> = StatefulStore(ViewState(sharedPreferencesManager.selectedCurrency), viewModelScope)
    val viewState = statefulStore.state

    fun onAction(action: Action) {
        when (action) {
            is Action.ChangeCurrency -> changeCurrency(action.currency)
        }
    }

    private fun changeCurrency(currency: Currency) {
        sharedPreferencesManager.selectedCurrency = currency
        statefulStore.process { oldState -> oldState.copy(selectedCurrency = currency) }
    }

    data class ViewState(
        val selectedCurrency: Currency? = null
    )

    sealed interface Action {
        data class ChangeCurrency(val currency: Currency) : Action
    }
}