package com.example.dmz.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Keywords(
    val keyText: String,
    val keyImage: Int,
    val recentTrend: List<Int>
): Parcelable
