package com.example.cryptopricehistory.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("pagingKeys")
data class PagingKeys(
    val prevPage: Long?,
    @PrimaryKey val tradingDataId: Long,
    val nextPage: Long?
)
