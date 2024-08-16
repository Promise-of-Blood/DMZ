package com.example.dmz.utils

import android.icu.text.DecimalFormat
import androidx.core.text.isDigitsOnly
import java.time.LocalDateTime
import java.time.ZoneId
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

    fun String.formatDiffTime(): String {
        val formatterBuilder = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .toFormatter()
        val start = LocalDateTime.parse(this, formatterBuilder) ?: LocalDateTime.now()
        val current = LocalDateTime.now()
        return when (val diff = (current.toLong() - start.toLong()) / 1000) {
            in 0..59 -> "방금 전" // 60
            in 60..3599 -> "${diff / 60}분 전" // 60 * 60
            in 3600..86399 -> "${diff / 3600}시간 전" // 60 * 60 * 24
            in 86400..604799 -> "${diff / 86400}일 전" // 60 * 60 * 24 * 7
            else -> this.formatDate()
        }
    }

    fun String.formatDiffDay(): String {
        val formatterBuilder = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .toFormatter()
        val start = LocalDateTime.parse(this, formatterBuilder) ?: LocalDateTime.now()
        val current = LocalDateTime.now()
        val dayMilli = 1000 * 60 * 60 * 24
        return when (val day = (current.toLong() - start.toLong()) / dayMilli) {
            in 0..30 -> "${day}일"
            in 31..364 -> "${day / 30}달"
            else -> "${day / 365}년"
        }
    }

    private fun LocalDateTime.toLong(): Long {
        return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}