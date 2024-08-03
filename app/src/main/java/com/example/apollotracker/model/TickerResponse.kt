package com.example.apollotracker.model

import com.google.gson.annotations.SerializedName

data class TickerResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("price_usd") val priceUsd: Double,
    @SerializedName("last_updated") val lastUpdated: String
)

