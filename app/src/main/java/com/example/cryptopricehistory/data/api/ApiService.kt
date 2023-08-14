package com.example.cryptopricehistory.data.api

import com.example.cryptopricehistory.data.model.TradingData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v3/klines")
    suspend fun getTradingData(
        @Query("symbol") currency: String,
        @Query("startTime") startTime: Long,
        @Query("interval") interval: String = "1h",
        @Query("limit") limit: Int = 10
    ): List<TradingData>
}