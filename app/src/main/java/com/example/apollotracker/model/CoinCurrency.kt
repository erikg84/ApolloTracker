package com.example.apollotracker.model

import com.google.gson.annotations.SerializedName

data class CoinCurrency(
	@SerializedName("price")
	val price: Float? = null
)