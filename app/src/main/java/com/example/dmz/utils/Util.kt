package com.example.dmz.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources.getSystem
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.util.Log
import androidx.core.text.isDigitsOnly
import com.example.dmz.MainActivity
import com.example.dmz.R
import com.example.dmz.model.SearchEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Util {
    val Float.px get() = (this * getSystem().displayMetrics.density).toInt()

    /**
     * ISO 8601(yyyy-MM-ddTHH:mm:ssZ) 형식의 날짜를 yyyy.MM.dd 형식으로 변환합니다.
     *
     * @return String ISO 8601(yyyy-MM-ddTHH:mm:ssZ) 형식의 날짜를 yyyy.MM.dd 형식으로 변환한 문자열
     */
    fun String.formatDate(): String {
        val formatterBuilder = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .toFormatter()
        val localDateTime = LocalDateTime.parse(this, formatterBuilder) ?: LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return localDateTime.format(formatter)
    }

    /**
     * 문자열이 나타내는 숫자를 천 단위, 만 단위로 표현합니다.
     *
     * - 0 ~ 999: 숫자 그대로
     * - 1000 ~ 9999: #.#천
     * - 10000 이상: #.#만
     *
     * @return String 문자열이 나타내는 숫자를 쉼표가 찍힌 천 단위, 만 단위로 표현한 문자열
     */
    fun String.formatNumber(): String {
        if (!this.isDigitsOnly()) return ""
        val decimalFormat = DecimalFormat("#,###.0")
        return when (val number = this.toLong()) {
            in 0..999 -> number.toString()
            in 1000..9999 -> "${decimalFormat.format(number / 1000.0)}천"
            else -> "${decimalFormat.format(number / 10000.0)}만"
        }
    }

    /**
     * 현재 날짜에서, 입력된 날짜까지의 시간 차이를 표현합니다.
     * 이때, 문자열은 ISO 8601(yyyy-MM-ddTHH:mm:ssZ) 형식이어야 합니다.
     *
     * - 0초 ~ 59초: 방금 전
     * - 1분 ~ 59분: #분 전
     * - 1시간 ~ 23시간: #시간 전
     * - 1일 ~ 7일: #일 전
     * - 8일 이상: yyyy.MM.dd
     *
     * @return String 현재 날짜와 문자열이 나타내는 날짜의 시간 차이를 분, 시간, 일 단위로 나타낸 문자열
     */
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

    /**
     * 현재 날짜에서 문자열이 나타내는 날짜를 뺀 일 수를 계산합니다.
     * 이때, 문자열은 ISO 8601(yyyy-MM-ddTHH:mm:ssZ) 형식이어야 합니다.
     *
     * - 0일 ~ 30일: #일
     * - 31일 ~ 364일: #달
     * - 365일 이상: #년
     *
     * @return String 현재 날짜와 문자열이 나타내는 날짜의 차이를 일, 달, 년 단위로 나타낸 문자열
     */
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

    /**
     * LocalDateTime 형식의 날짜를 Long 타입으로 변환합니다.
     */
    private fun LocalDateTime.toLong(): Long {
        return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun LocalDateTime.toISO8601(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return this.format(formatter)
    }

    fun MainActivity.handleBottomNavigationVisibility(isShow: Boolean) {
        this.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility =
            if (isShow) View.VISIBLE else View.GONE
        this.findViewById<ImageView>(R.id.iv_home_btn)?.visibility =
            if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * @return 현재시간을 ISO 8601(yyyy-MM-ddTHH:mm:ssZ) 형식으로 반환합니다.
     */
    fun getNowTimeAsIso(): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREAN)
        val formattedDate = isoFormat.format(Date())
        Log.d("formattedDate", formattedDate)
        return formattedDate
    }

    /**
     * 기간 설정시 사용되는 type들
     */
    enum class DateType {
        DATE,
        MONTH,
        YEAR
    }

    /**
     * 설정된 날짜들 만큼 빼주는 함수
     *
     * @property nowDate 계산할 날짜
     * @property type 기간의 단위 설정(ex. DATE - 며칠을 뺄지, MONTH - 몇 달을 뺄지, YEAR - 몇 년을 뺄지)
     * @property ago 뺄 기간을 설정
     */
    fun setDateAgo(nowDate: String, type: DateType, ago: Int): String {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = df.parse(nowDate)
        if (date != null) {
            cal.time = date
        }
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

    fun addPrefItem(context: Context, item: SearchEntity) {
        val prefs = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        val gson = GsonBuilder().create()
        editor.putString(item.query, gson.toJson(item))
        editor.apply()
    }

    fun deletePrefItem(context: Context, query: String) {
        val prefs = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.remove(query)
        editor.apply()
    }

    fun getPrefRecentSearchList(context: Context): ArrayList<SearchEntity> {
        val prefs = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Activity.MODE_PRIVATE
        )
        val allEntries: Map<String, *> = prefs.all
        val searchItems = ArrayList<SearchEntity>()
        val gson = GsonBuilder().create()
        for ((key, value) in allEntries) {
            val item = gson.fromJson(value as String, SearchEntity::class.java)
            searchItems.add(item)
        }
        return searchItems
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
}
