package com.example.cryptopricehistory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptopricehistory.data.api.repository.TradingDataRepository
import com.example.cryptopricehistory.data.model.TradingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradingDataViewModel @Inject constructor(
    private val repository: TradingDataRepository
) : ViewModel() {

    private val _tradingDataFlow = MutableStateFlow(listOf<TradingData>())

    val tradingDataFlow: StateFlow<List<TradingData>> = _tradingDataFlow

    init {
        viewModelScope.launch {
            _tradingDataFlow.value = repository.getTradingData("BTCUSDT")
        }
    }

}