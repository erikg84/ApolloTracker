package com.example.apollotracker.model

import com.google.gson.annotations.SerializedName

data class AltCoinResponseItem(
	@SerializedName("id")
	val id: String? = null,

	@SerializedName("symbol")
	val symbol: String? = null,

	@SerializedName("last_updated")
	val lastUpdated: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("quotes")
	val quotes: Quotes? = null
)