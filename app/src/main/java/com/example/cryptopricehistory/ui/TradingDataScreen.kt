package com.example.cryptopricehistory.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingDataScreen(modifier: Modifier = Modifier) {
    val viewModel: TradingDataViewModel = hiltViewModel()
    val tradingDataItems = viewModel.tradingDataFlow.collectAsLazyPagingItems()
    val currentSearchState = viewModel.currentSearchFlow.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text(text = "Crypto price history") }) }) { padding ->
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
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxHeight()
            ) {
                if (tradingDataItems.loadState.prepend is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(modifier = modifier.padding(vertical = 8.dp))
                    }
                }
                if (tradingDataItems.loadState.refresh is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(modifier = modifier.padding(vertical = 8.dp))
                    }
                } else {
                    items(tradingDataItems, key = { it.openTime }) { tradingData ->
                        tradingData?.let {
                            Column(modifier = modifier.fillMaxWidth()) {
                                Row(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = tradingData.openPrice.toString(), fontSize = 18.sp)
                                    Column {
                                        Text(text = tradingData.openTime.toDateString("MM.dd.yy HH:mm"))
                                        Text(text = "High: ${tradingData.highPrice}")
                                        Text(text = "Low: ${tradingData.lowPrice}")
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val color =
                                            if (tradingData.priceWentDown) Color.Red else Color(
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
                                            text = "${
                                                String.format(
                                                    "%.2f",
                                                    tradingData.priceChange
                                                )
                                            }%",
                                            fontSize = 18.sp, color = color
                                        )
                                    }
                                }
                            }
                            Divider()
                        }

                    }
                }

                if (tradingDataItems.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(modifier = modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }

}