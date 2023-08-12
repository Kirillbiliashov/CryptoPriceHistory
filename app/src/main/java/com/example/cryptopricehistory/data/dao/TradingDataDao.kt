package com.example.cryptopricehistory.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptopricehistory.data.model.TradingData

@Dao
interface TradingDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradingDataList(tradingData: List<TradingData>)

    @Delete
    suspend fun deleteTradingDataList(tradingData: List<TradingData>)

    @Query("SELECT * FROM tradingData ORDER BY openTime DESC")
    fun getTradingData(): PagingSource<Int, TradingData>
}