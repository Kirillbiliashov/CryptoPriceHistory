package com.example.cryptopricehistory.data.api

import com.example.cryptopricehistory.data.model.TradingData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v3/klines")
    suspend fun getTradingData(
        @Query("symbol") currency: String,
        @Query("interval") interval: String = "1h"
    ): List<TradingData>
}