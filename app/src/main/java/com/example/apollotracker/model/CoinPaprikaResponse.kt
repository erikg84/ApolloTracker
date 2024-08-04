package com.example.apollotracker.model

import com.google.gson.annotations.SerializedName

data class CoinPaprikaResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("symbol")
    val symbol: String? = null,

    @SerializedName("rank")
    val rank: Int? = null,

    @SerializedName("total_supply")
    val totalSupply: Double? = null,

    @SerializedName("max_supply")
    val maxSupply: Double? = null,

    @SerializedName("beta_value")
    val betaValue: Double? = null,

    @SerializedName("first_data_at")
    val firstDataAt: String? = null,

    @SerializedName("last_updated")
    val lastUpdated: String? = null,

    @SerializedName("quotes")
    val quotes: Map<String, Quote>? = null
)
