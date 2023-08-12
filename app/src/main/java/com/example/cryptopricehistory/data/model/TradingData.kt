package com.example.cryptopricehistory.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("tradingData")
data class TradingData(
    @PrimaryKey
    val openTime: Long,
    val openPrice: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val closePrice: Double,
    val numberOfTrades: Int
) {
    @Ignore
    val priceChange = ((closePrice - openPrice) / openPrice) * 100
    @Ignore
    val priceWentDown = closePrice < openPrice
}