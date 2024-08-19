package com.example.dmz.data

import com.example.dmz.R
import com.example.dmz.data.model.Keywords

fun keywordsList(): List<Keywords> {
    return listOf(
        Keywords(
            "요아정",
            R.drawable.img_keyword_yoajung,
            listOf(1, 5, 3, 4, 3, 0, 0, 0, 2, 1, 5, 2, 2, 4)
        ),
        Keywords(
            "두바이 초콜릿",
            R.drawable.img_keyword_yoajung,
            listOf(3, 2, 1, 4, 6, 4, 2, 1, 5, 6, 2, 1, 3, 5)
        ),
        Keywords(
            "요아정3",
            R.drawable.img_keyword_yoajung,
            listOf(5, 2, 1, 5, 1, 0, 2, 0, 1, 2, 0, 0, 4, 5)
        ),
        Keywords(
            "요아정4",
            R.drawable.img_keyword_yoajung,
            listOf(0, 0, 0, 0, 0, 2, 4, 5, 5, 4, 5, 4, 2, 5)
        ),
        Keywords(
            "요아정5",
            R.drawable.img_keyword_yoajung,
            listOf(3, 2, 2, 4, 0, 1, 2, 2, 4, 5, 5, 1, 2, 3)
        ),
        Keywords(
            "요아정6",
            R.drawable.img_keyword_yoajung,
            listOf(0, 0, 1, 0, 2, 0, 4, 4, 5, 5, 3, 4, 5, 5)
        ),
    )
}