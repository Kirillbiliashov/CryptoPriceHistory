package com.example.cryptopricehistory.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TradingDataScreen(modifier: Modifier = Modifier) {
    val viewModel: TradingDataViewModel = hiltViewModel()
}