package com.example.cryptopricehistory.data.api.repository

import com.example.cryptopricehistory.data.api.ApiService
import com.example.cryptopricehistory.data.model.TradingData
import javax.inject.Inject
import javax.inject.Singleton

interface TradingDataRepository {
    suspend fun getTradingData(currency: String): List<TradingData>
}

@Singleton
class TradingDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TradingDataRepository {
    override suspend fun getTradingData(currency: String) =
        apiService.getTradingData(currency)

}