package com.example.apollotracker.testutil

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
class FlowTestObserver<T>(scope: TestScope, flow: Flow<T>) {

    val changes = mutableListOf<T>()

    private val job = scope.launch(UnconfinedTestDispatcher()) {
        flow.collect {
            changes.add(it)
        }
    }

    fun cancelAndRemoveRemainingEvents(events: (List<T>) -> Unit) {
        job.cancel()
        changes.clear()
        events(changes)
    }
}

@ExperimentalCoroutinesApi
fun <T> StateFlow<T>.test(scope: TestScope): FlowTestObserver<T> {
    return FlowTestObserver(scope, this)
}

@ExperimentalCoroutinesApi
fun <T> Flow<T>.test(scope: TestScope): FlowTestObserver<T> {
    return FlowTestObserver(scope, this)
}

