package com.example.cryptopricehistory.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object UtilFunctions {
    @SuppressLint("SimpleDateFormat")
    fun Long.toDateString(format: String) =
        SimpleDateFormat(format).format(Date(this))
}