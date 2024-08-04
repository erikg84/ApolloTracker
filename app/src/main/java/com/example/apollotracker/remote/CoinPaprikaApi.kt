package com.example.apollotracker.remote

import com.example.apollotracker.model.AltCoinResponseItem
import com.example.apollotracker.model.CoinPaprikaResponse
import com.example.apollotracker.model.HistoricalDataPoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinPaprikaApi {

    @GET("v1/tickers/btc-bitcoin")
    suspend fun getBitcoinInfo(@Query("quotes") currency: String): Response<CoinPaprikaResponse>

    @GET("v1/tickers")
    suspend fun getAltcoinInfo(@Query("quotes") currency: String): Response<List<AltCoinResponseItem>>

    @GET("v1/tickers/{coin_id}/historical")
    suspend fun getHistoricalInfo(
        @Path("coin_id") coinId: String,
        @Query("start") startDate: String,
        @Query("interval") interval: String
    ): Response<List<HistoricalDataPoint>>
}
