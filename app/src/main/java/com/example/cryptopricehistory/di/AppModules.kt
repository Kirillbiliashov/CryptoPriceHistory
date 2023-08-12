package com.example.cryptopricehistory.di

import android.content.Context
import androidx.room.Room
import com.example.cryptopricehistory.data.api.ApiService
import com.example.cryptopricehistory.data.api.repository.TradingDataRepository
import com.example.cryptopricehistory.data.api.repository.TradingDataRepositoryImpl
import com.example.cryptopricehistory.data.dao.TradingDataDao
import com.example.cryptopricehistory.data.db.TradingDataDatabase
import com.example.cryptopricehistory.data.model.TradingData
import com.example.cryptopricehistory.utils.TradingDataDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTradingDataRepository(
        repositoryImpl: TradingDataRepositoryImpl
    ): TradingDataRepository
}

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private val URL = "https://api.binance.com/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(
                        TradingData::class.java,
                        TradingDataDeserializer()
                    )
                    .create()
            )
        )
        .build()

    @Provides
    fun provideApiService() = retrofit.create(ApiService::class.java)

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext ctxt: Context) = Room
        .databaseBuilder(ctxt, TradingDataDatabase::class.java, "trading_data")
        .build()

    @Provides
    fun provideTradingDataDao(database: TradingDataDatabase) = database.tradingDataDao()

    @Provides
    fun providePagingKeysDao(database: TradingDataDatabase) = database.pagingKeysDao()

}