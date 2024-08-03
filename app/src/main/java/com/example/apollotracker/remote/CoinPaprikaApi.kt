package com.example.apollotracker.remote

import com.example.apollotracker.model.TickerResponse
import retrofit2.http.GET

interface CoinPaprikaApi {

    @GET("v1/tickers/btc-bitcoin")
    suspend fun getBitcoinPrice(): TickerResponse

    @GET("v1/tickers")
    suspend fun getAltcoinPrices(): List<TickerResponse>
}
