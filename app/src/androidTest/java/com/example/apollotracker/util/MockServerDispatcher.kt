package com.example.apollotracker.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/v1/tickers/btc-bitcoin?quotes=USD" -> {
                MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("bitcoin.json"))
            }
            "/v1/tickers?quotes=USD" -> {
                MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("altcoin.json"))
            }
            "/v1/tickers/btc-bitcoin/historical?start=2023-08-06&interval=1d" -> {
                MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("historical.json"))
            }
            else -> MockResponse().setResponseCode(404)
        }
    }
}
