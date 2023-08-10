package com.example.cryptopricehistory.utils

import com.example.cryptopricehistory.data.model.TradingData
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class TradingDataDeserializer: JsonDeserializer<TradingData> {

    private val OPEN_TIME_IDX = 0
    private val OPEN_PRICE_IDX = 1
    private val HIGH_PRICE_IDX = 2
    private val LOW_PRICE_IDX = 3
    private val CLOSE_PRICE_IDX = 4
    private val NUMBER_OF_TRADES_IDX = 8


    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TradingData {
        val jsonArray = json?.asJsonArray
        val openTime = jsonArray?.get(OPEN_TIME_IDX)?.asLong ?: 0
        val openPrice = jsonArray?.get(OPEN_PRICE_IDX)?.asDouble ?: 0.0
        val highPrice = jsonArray?.get(HIGH_PRICE_IDX)?.asDouble ?: 0.0
        val lowPrice = jsonArray?.get(LOW_PRICE_IDX)?.asDouble ?: 0.0
        val closePrice = jsonArray?.get(CLOSE_PRICE_IDX)?.asDouble ?: 0.0
        val numberOfTrades = jsonArray?.get(NUMBER_OF_TRADES_IDX)?.asInt ?: 0
        return TradingData(
            openTime,
            openPrice,
            highPrice,
            lowPrice,
            closePrice,
            numberOfTrades
        )
    }

}