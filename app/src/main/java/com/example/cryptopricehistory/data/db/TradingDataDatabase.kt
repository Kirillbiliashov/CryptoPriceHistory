package com.example.cryptopricehistory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptopricehistory.data.dao.PagingKeysDao
import com.example.cryptopricehistory.data.dao.TradingDataDao
import com.example.cryptopricehistory.data.model.PagingKeys
import com.example.cryptopricehistory.data.model.TradingData

@Database(entities = [TradingData::class, PagingKeys::class], version = 3)
abstract class TradingDataDatabase: RoomDatabase() {

    abstract fun tradingDataDao(): TradingDataDao

    abstract fun pagingKeysDao(): PagingKeysDao

}