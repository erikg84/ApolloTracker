package com.example.apollotracker.model

import com.google.gson.annotations.SerializedName

data class Quotes(
	@SerializedName("USD")
	val uSD: CoinCurrency? = null,
	@SerializedName("GBP")
	val gBP: CoinCurrency? = null,
	@SerializedName("EUR")
	val eUR: CoinCurrency? = null
) {
	fun getCurrency(currency: Currency): CoinCurrency? {
		return when (currency) {
			Currency.USD -> uSD
			Currency.GBP -> gBP
			Currency.EUR -> eUR
		}
	}
}