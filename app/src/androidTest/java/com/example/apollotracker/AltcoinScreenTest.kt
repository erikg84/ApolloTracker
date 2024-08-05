package com.example.apollotracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.apollotracker.di.UrlModule
import com.example.apollotracker.util.MockServerDispatcher
import com.example.apollotracker.view.MainActivity
import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(UrlModule::class)
@RunWith(AndroidJUnit4::class)
class AltcoinScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var okHttp: OkHttpClient

    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    private val mockWebServer = MockWebServer().apply {
        dispatcher = MockServerDispatcher()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class FakeBaseUrlModule {

        @Provides
        @Singleton
        fun provideUrl(): String = "http://127.0.0.1:8080/"
    }

    @Before
    fun setUp(){
        hiltRule.inject()
        okHttp3IdlingResource = OkHttp3IdlingResource.create("okhttp", okHttp)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
        mockWebServer.start(8080)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

    @Test
    fun testAltcoinScreenDisplays() {
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("MainScreenTest")

        composeTestRule.waitUntil(2000L) {
            composeTestRule.onNodeWithTag("MainScreen").isDisplayed()
        }

        composeTestRule.onNodeWithTag("AltcoinDestinationButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntil {
            composeTestRule.onNodeWithTag("AltcoinScreen").isDisplayed()
        }
    }

    @Test
    fun testAltcoinScreenScrollUpAndDownOnList() {
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("MainScreenTest")

        composeTestRule.waitUntil(2000L) {
            composeTestRule.onNodeWithTag("MainScreen").isDisplayed()
        }

        composeTestRule.onNodeWithTag("AltcoinDestinationButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntil {
            composeTestRule.onNodeWithTag("AltcoinScreen").isDisplayed()
        }

        composeTestRule.onNodeWithTag("AltcoinList")
            .performScrollToIndex(10)

        composeTestRule.onNodeWithTag("AltCoinItem10")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("AltcoinList")
            .performScrollToIndex(0)

        composeTestRule.onNodeWithTag("AltCoinItem0")
            .assertIsDisplayed()
    }

    @Test
    fun testAltcoinScreenClickonItemViewGraphLink() {
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("MainScreenTest")

        composeTestRule.waitUntil(2000L) {
            composeTestRule.onNodeWithTag("MainScreen").isDisplayed()
        }

        composeTestRule.onNodeWithTag("AltcoinDestinationButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntil {
            composeTestRule.onNodeWithTag("AltcoinScreen").isDisplayed()
        }

        val itemIndex = 0

        composeTestRule.onNodeWithTag("ViewGraphLinkAltCoinItem$itemIndex")
            .performClick()

        composeTestRule.waitUntil {
            composeTestRule.onNodeWithTag("GraphScreen").isDisplayed()
        }
    }
}