package com.example.myapplication.features.time.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ServerTime(val epochMillis: Long) {
    fun format(pattern: String = "HH:mm:ss"): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(Date(epochMillis))
    }
}
