package com.example.apollotracker.model

import com.google.gson.annotations.SerializedName

data class HistoricalDataPoint(
    @SerializedName("timestamp")
    val timestamp: String? = null,

    @SerializedName("price")
    val price: Float? = null,

    @SerializedName("volume_24h")
    val volume24h: Long? = null,

    @SerializedName("market_cap")
    val marketCap: Long? = null
)

