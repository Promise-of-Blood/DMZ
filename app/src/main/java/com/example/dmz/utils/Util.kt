package com.example.dmz.utils

import android.icu.text.DateFormat
import android.icu.text.DecimalFormat
import android.util.Log
import androidx.core.text.isDigitsOnly
import com.example.dmz.ui.search.SearchFragment
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

    // 현재시간이 ISO 포맷으로 리턴
    fun getNowTimeAsIso(): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREAN)
        val formattedDate = isoFormat.format(Date())
        Log.d("formattedDate", formattedDate)
        return formattedDate
    }

    enum class DateType {
        DATE,
        MONTH,
        YEAR
    }

    fun setDateAgo(nowDate: String, type: DateType, ago: Int): String {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = df.parse(nowDate)
        cal.time = date
        Log.d("current", df.format(cal.time))

        when (type) {
            DateType.DATE -> {
                cal.add(Calendar.DATE, -ago)
            }

            DateType.MONTH -> {
                cal.add(Calendar.MONTH, -ago)
            }

            DateType.YEAR -> {
                cal.add(Calendar.YEAR, -ago)
            }
        }

        Log.d("after", df.format(cal.time))
        return df.format(cal.time)
    }


    fun isoToDate(time: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREAN)
        val date = format.parse(time)
        return date
    }

    fun isoToString(time: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREAN)
        val date = format.parse(time)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val dateString: String = formatter.format(date!!)
        return dateString
    }

//    fun getDateAgoFromNow(): String {
//        val cal = Calendar.getInstance()
//        cal.time = Date()
//        val isoFormat = SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss.SSSZ", Locale.KOREAN)
//        Log.d("current :", isoFormat.format(cal.time))
//
//        cal.add(Calendar.MONTH, 2)
//        cal.add(Calendar.DATE, -3)
//
//        Log.d("after", isoFormat.format(cal.time))
//
//        return ""
//    }
}





