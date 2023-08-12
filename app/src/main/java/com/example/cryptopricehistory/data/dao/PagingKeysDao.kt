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

    @Delete
    suspend fun deletePagingKeysList(pagingKeys: List<PagingKeys>)

    @Query("SELECT * FROM pagingKeys")
    fun getPagingKeysList(): List<PagingKeys>

}