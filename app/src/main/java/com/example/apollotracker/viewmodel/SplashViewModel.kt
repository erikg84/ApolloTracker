package com.example.apollotracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollotracker.navigation.Main
import com.example.apollotracker.navigation.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val router: Router
) : ViewModel() {

    fun onAction(action: Action) {
        when (action) {
            is Action.StartTimer -> startTimer()
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_DURATION)
            router.navigateTo(Main)
        }
    }

    sealed interface Action {
        data object StartTimer : Action
    }

    companion object {
        private const val SPLASH_SCREEN_DURATION = 2000L
    }
}