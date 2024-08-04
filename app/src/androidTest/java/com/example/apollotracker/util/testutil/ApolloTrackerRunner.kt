package com.example.apollotracker.util.testutil

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class ApolloTrackerRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return InstrumentationRegistry.getInstrumentation().newApplication(
            cl, HiltTestApplication::class.java.name, context
        )
    }
}