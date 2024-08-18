package com.example.dmz.utils

import android.animation.ObjectAnimator
import android.icu.text.DecimalFormat
import android.view.View
import androidx.core.text.isDigitsOnly
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import kotlin.random.Random

object Util {
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

    fun wiggle(view: View, duration:Long, delay: Long){
        val firstValueY = Random.nextFloat()*30-30
        val secondValueY = Random.nextFloat()*20-0
        val animator = ObjectAnimator.ofFloat(view, "translationY", view.translationY+0f, view.translationY+firstValueY, view.translationY+secondValueY)
        animator.duration = duration
        animator.startDelay = delay
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }
}