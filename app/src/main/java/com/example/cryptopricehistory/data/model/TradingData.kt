package com.example.cryptopricehistory.data.model

import com.google.gson.annotations.SerializedName

data class TradingData(
    @SerializedName("0")
    val openTime: Long,
    @SerializedName("1")
    val openPrice: Double,
    @SerializedName("2")
    val highPrice: Double,
    @SerializedName("3")
    val lowPrice: Double,
    @SerializedName("4")
    val closePrice: Double,
    @SerializedName("8")
    val numberOfTrades: Int
)
