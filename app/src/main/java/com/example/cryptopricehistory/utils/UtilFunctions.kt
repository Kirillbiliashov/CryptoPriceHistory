package com.example.cryptopricehistory.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

object UtilFunctions {
    @SuppressLint("SimpleDateFormat")
    fun Long.toDateString(format: String) =
        SimpleDateFormat(format).format(Date(this))

    @SuppressLint("NewApi")
    fun getStartTimeAtHourWithOffset(offset: Int): Long {
        val currentTime = LocalDateTime.now(ZoneOffset.UTC)
        val currentHour = currentTime.hour
        val beginningOfHour =
            currentTime.withHour(currentHour).withMinute(0).withSecond(0).withNano(0)
        return beginningOfHour.toInstant(ZoneOffset.UTC).toEpochMilli() - 3_600_000 * offset
    }

}