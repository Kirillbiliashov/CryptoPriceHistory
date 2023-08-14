package com.example.cryptopricehistory.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.cryptopricehistory.data.api.ApiService
import com.example.cryptopricehistory.data.dao.PagingKeysDao
import com.example.cryptopricehistory.data.db.TradingDataDatabase
import com.example.cryptopricehistory.data.model.PagingKeys
import com.example.cryptopricehistory.data.model.TradingData
import com.example.cryptopricehistory.utils.UtilFunctions
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

const val ONE_HOUR_IN_MSEC = 3600000

@OptIn(ExperimentalPagingApi::class)
class TradingDataRemoteMediator @Inject constructor(
    private val currency: String,
    val db: TradingDataDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, TradingData>() {

    private val pagingKeysDao = db.pagingKeysDao()
    private val tradingDataDao = db.tradingDataDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TradingData>
    ): MediatorResult {
        val limit = state.config.pageSize
        val defaultStartTime = UtilFunctions.getStartTimeAtHourWithOffset(limit)
        val startTime = when (loadType) {
            LoadType.PREPEND -> {
                val pagingKeys = state.getPrependPagingKeys(pagingKeysDao)
                pagingKeys?.prevPage
                    ?: return MediatorResult.Success(endOfPaginationReached = pagingKeys != null)
            }
            LoadType.REFRESH -> {
                val pagingKeys = state.getRefreshPagingKeys(pagingKeysDao)
                pagingKeys?.nextPage?.plus(ONE_HOUR_IN_MSEC * limit) ?: defaultStartTime
            }
            LoadType.APPEND -> {
                val pagingKeys = state.getAppendPagingKeys(pagingKeysDao)
                pagingKeys?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = pagingKeys != null)
            }
        }
        return try {
            val tradingData = apiService.getTradingData(currency, startTime, limit = limit)
            val isEndOfPagination = tradingData.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    tradingDataDao.deleteTradingDataList()
                    pagingKeysDao.deletePagingKeysList()
                }
                val nextStartTime = if (isEndOfPagination) null
                else startTime - ONE_HOUR_IN_MSEC * limit
                val prevStartTime = if (defaultStartTime == startTime) null
                else startTime + ONE_HOUR_IN_MSEC * limit
                val pagingKeysList = tradingData.map {
                    PagingKeys(
                        prevPage = prevStartTime,
                        tradingDataId = it.openTime,
                        nextPage = nextStartTime
                    )
                }
                pagingKeysDao.insertPagingKeysList(pagingKeysList)
                tradingDataDao.insertTradingDataList(tradingData)
            }
            MediatorResult.Success(endOfPaginationReached = isEndOfPagination)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: IOException) {
            println(e.message)
            MediatorResult.Error(e)
        }
    }

    private suspend fun PagingState<Int, TradingData>.getPrependPagingKeys(
        dao: PagingKeysDao
    ): PagingKeys? {
        val openTime = this.firstItemOrNull()?.openTime ?: return null
        return dao.getPagingKey(openTime)
    }

    private suspend fun PagingState<Int, TradingData>.getAppendPagingKeys(
        dao: PagingKeysDao
    ): PagingKeys? {
        val openTime = this.lastItemOrNull()?.openTime ?: return null
        return dao.getPagingKey(openTime)
    }

    private suspend fun PagingState<Int, TradingData>.getRefreshPagingKeys(
        dao: PagingKeysDao
    ): PagingKeys? {
        val anchorPosition = this.anchorPosition ?: return null
        val openTime = this.closestItemToPosition(anchorPosition)?.openTime ?: return null
        return dao.getPagingKey(openTime)
    }


}