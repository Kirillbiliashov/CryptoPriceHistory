package com.example.cryptopricehistory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptopricehistory.data.api.repository.TradingDataRepository
import com.example.cryptopricehistory.data.model.TradingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class TradingDataViewModel @Inject constructor(
    private val repository: TradingDataRepository
) : ViewModel() {

    private val _currentSearchFlow = MutableStateFlow("")
    val currentSearchFlow: StateFlow<String> = _currentSearchFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ""
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val tradingDataFlow = _currentSearchFlow.flatMapLatest { currency ->
        flow {
            try {
                val data = repository.getTradingData(currency)
                emit(data)
            } catch (e: HttpException) {
                emit(listOf())
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )

    fun updateSearchTextFieldValue(newValue: String) {
        _currentSearchFlow.value = newValue
    }

}