package com.example.cryptopricehistory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.cryptopricehistory.data.repository.TradingDataRepository
import com.example.cryptopricehistory.data.model.TradingData
import com.example.cryptopricehistory.data.paging.UiModel
import com.example.cryptopricehistory.utils.UtilFunctions.toDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class TradingDataViewModel @Inject constructor(
    private val repository: TradingDataRepository
) : ViewModel() {

    private val _currentSearchFlow = MutableStateFlow("BTCUSDT")
    val currentSearchFlow: StateFlow<String> = _currentSearchFlow

    private val _isErrorFlow = MutableStateFlow(false)
    val isErrorFlow: StateFlow<Boolean> = _isErrorFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    val tradingDataFlow = _currentSearchFlow.flatMapLatest { currency ->
        repository.getTradingDataFlow(currency)
    }
        .map { pagingData ->
            pagingData.map { UiModel.TradingDataItem(it) }
                .insertSeparators { before, after ->
                    if (after == null) {
                        return@insertSeparators null
                    }
                    val afterDateStr = after.tradingData.openTime.toDateString("MM.dd.yy")
                    if (before == null) {
                        return@insertSeparators UiModel.Separator(afterDateStr)
                    }
                    val beforeDateStr = before.tradingData.openTime.toDateString("MM.dd.yy")
                    return@insertSeparators if (beforeDateStr == afterDateStr) null
                    else UiModel.Separator(afterDateStr)
                }
        }
        .cachedIn(viewModelScope)

    fun updateSearchTextFieldValue(newValue: String) {
        _currentSearchFlow.value = newValue
    }

    fun setError(isError: Boolean) {
        _isErrorFlow.value = isError
    }

}