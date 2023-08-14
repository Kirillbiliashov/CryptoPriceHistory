package com.example.cryptopricehistory.data.paging

import com.example.cryptopricehistory.data.model.TradingData

sealed class UiModel {
    class TradingDataItem(val tradingData: TradingData) : UiModel()
    class Separator(val date: String) : UiModel()
}
