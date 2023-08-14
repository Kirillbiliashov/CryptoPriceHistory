package com.example.cryptopricehistory.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptopricehistory.data.api.ApiService
import com.example.cryptopricehistory.data.db.TradingDataDatabase
import com.example.cryptopricehistory.data.model.TradingData
import com.example.cryptopricehistory.data.paging.TradingDataRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface TradingDataRepository {
    suspend fun getTradingDataFlow(currency: String): Flow<PagingData<TradingData>>
}

private val PAGE_SIZE = 20

@Singleton
class TradingDataRepositoryImpl @Inject constructor(
    private val db: TradingDataDatabase,
    private val apiService: ApiService
) : TradingDataRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getTradingDataFlow(currency: String) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true),
        remoteMediator = TradingDataRemoteMediator(
            currency,
            db,
            apiService
        )
    ) {
         db.tradingDataDao().getTradingData()
    }.flow

}