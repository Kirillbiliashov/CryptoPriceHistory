package com.example.cryptopricehistory.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptopricehistory.data.model.PagingKeys
import com.example.cryptopricehistory.data.model.TradingData

@Dao
interface PagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPagingKeysList(pagingKeys: List<PagingKeys>)

    @Query("DELETE  FROM pagingKeys")
    suspend fun deletePagingKeysList()

    @Query("SELECT * FROM pagingKeys WHERE tradingDataId = :openTime")
    suspend fun getPagingKey(openTime: Long): PagingKeys

}