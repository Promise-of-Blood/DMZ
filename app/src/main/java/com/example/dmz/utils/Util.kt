package com.example.dmz.utils

import android.icu.text.DecimalFormat
import androidx.core.text.isDigitsOnly
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

object Util {
    fun String.formatDate(): String {
        val formatterBuilder = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .toFormatter()
        val localDateTime = LocalDateTime.parse(this, formatterBuilder) ?: LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return localDateTime.format(formatter)
    }

    fun String.formatNumber(): String {
        if (!this.isDigitsOnly()) return ""
        val decimalFormat = DecimalFormat("#,###.0")
        return when (val number = this.toLong()) {
            in 0..999 -> number.toString()
            in 1000..9999 -> "${decimalFormat.format(number / 1000.0)}천"
            else -> "${decimalFormat.format(number / 10000.0)}만"
        }
    }
}