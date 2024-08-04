package com.example.apollotracker.store

import com.example.apollotracker.testutil.TestCoroutineRule
import com.example.apollotracker.testutil.test
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StatefulStoreTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val statefulStore: StatefulStore<Int> by lazy {
        StatefulStore(0, testCoroutineRule.testScope)
    }

    @Test
    fun `initial state is correct`() = runTest {
        val observer = statefulStore.state.test(this)

        with(observer.changes.first()) {
            Assert.assertEquals(0, this)
        }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<Int>())
        }
    }

    @Test
    fun `process updates state correctly`() = runTest {
        val observer = statefulStore.state.test(this)

        statefulStore.process { it + 1 }

        with(observer.changes.last()) {
            Assert.assertEquals(1, this)
        }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<Int>())
        }
    }

    @Test
    fun `concurrent updates are handled correctly`() = runTest {
        val observer = statefulStore.state.test(this)

        val jobs = List(100) {
            launch {
                statefulStore.process { it + 1 }
            }
        }
        jobs.joinAll()

        with(observer.changes.last()) {
            Assert.assertEquals(100, this)
        }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<Int>())
        }
    }

    @Test
    fun `async updates are processed sequentially`() = runTest {
        val observer = statefulStore.state.test(this)

        statefulStore.process { it + 1 }
        statefulStore.process { it * 2 }
        statefulStore.process { it - 1 }

        with(observer.changes) {
            Assert.assertEquals(listOf(0, 1, 2, 1), this)
        }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<Int>())
        }
    }

    @Test
    fun `rapid updates are not dropped`() = runTest {
        val observer = statefulStore.state.test(this)

        val jobs = List(1000) {
            launch {
                statefulStore.process { it + 1 }
            }
        }
        jobs.joinAll()

        with(observer.changes.last()) {
            Assert.assertEquals(1000, this)
        }

        observer.cancelAndRemoveRemainingEvents { events ->
            Assert.assertEquals(events, emptyList<Int>())
        }
    }
}
