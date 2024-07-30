package com.example.apollotracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apollotracker.navigation.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    router: Router
): ViewModel() {
    val currentRoute = router.currentRoute
}