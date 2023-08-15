package com.example.cryptopricehistory.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.cryptopricehistory.utils.UtilFunctions.toDateString
import androidx.paging.compose.items
import com.example.cryptopricehistory.data.model.TradingData
import com.example.cryptopricehistory.data.paging.UiModel
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingDataScreen(modifier: Modifier = Modifier) {
    val viewModel: TradingDataViewModel = hiltViewModel()
    val uiItems = viewModel.tradingDataFlow.collectAsLazyPagingItems()
    val currentSearchState = viewModel.currentSearchFlow.collectAsState()
    val isErrorState = viewModel.isErrorFlow.collectAsState()
    Scaffold(topBar = { TradingDataTopBar() }) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = currentSearchState.value,
                onValueChange = viewModel::updateSearchTextFieldValue,
                label = { Text(text = "Currency pair") })
            Spacer(modifier = modifier.height(8.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxHeight()
            ) {
                val indicatorModifier = modifier.padding(vertical = 8.dp)
                when (val refresh = uiItems.loadState.refresh) {
                    is LoadState.Loading -> item {
                        viewModel.setError(false)
                        CircularProgressIndicator(modifier = indicatorModifier)
                    }

                    is LoadState.Error -> item {
                        viewModel.setError(true)
                        when (refresh.error) {
                            is HttpException ->
                                Text(
                                    text = "No result found for this query",
                                    fontSize = 18.sp
                                )

                            is IOException ->
                                TryAgainColumn(onTryClick = uiItems::retry)
                        }
                    }

                    is LoadState.NotLoading -> if (!isErrorState.value) {
                        uiItems.loadState.mediator
                        items(uiItems) { uiItem ->
                            when (uiItem) {
                                is UiModel.TradingDataItem ->
                                    TradingDataListItem(uiItem.tradingData)

                                is UiModel.Separator -> SeparatorItem(uiItem.date)

                                null -> {}
                            }
                            Divider()
                        }
                    }

                }
                when (uiItems.loadState.append) {
                    is LoadState.Loading -> item {
                        CircularProgressIndicator(modifier = indicatorModifier)
                    }

                    is LoadState.Error -> item {
                        TryAgainColumn(onTryClick = uiItems::retry, modifier = modifier.padding(8.dp))
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun TryAgainColumn(
    onTryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(text = "Error loading data", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onTryClick) {
            Text(text = "Try again")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingDataTopBar(modifier: Modifier = Modifier) {
    TopAppBar(title = { Text(text = "Crypto price history") })
}

@Composable
fun SeparatorItem(content: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(52.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = content, fontSize = 24.sp,
            modifier = modifier.padding(start = 16.dp)
        )
        Spacer(modifier = modifier.weight(1f))
    }
}

@Composable
fun TradingDataListItem(data: TradingData, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = data.openPrice.toString(), fontSize = 18.sp)
            Column {
                Text(text = data.openTime.toDateString("MM.dd.yy HH:mm"))
                Text(text = "High: ${data.highPrice}")
                Text(text = "Low: ${data.lowPrice}")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                val color =
                    if (data.priceWentDown) Color.Red else Color(
                        85,
                        201,
                        89,
                        255
                    )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = color
                )
                Text(
                    text = "${String.format("%.2f", data.priceChange)}%",
                    fontSize = 18.sp, color = color
                )
            }
        }
    }
}