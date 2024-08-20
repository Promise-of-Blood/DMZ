package com.example.dmz.data

import com.example.dmz.R
import com.example.dmz.data.model.Keywords

fun keywordsList(): List<Keywords> {
    return listOf(
        Keywords(
            "요아정",
            R.drawable.img_card_yoajung,
            listOf(1, 5, 3, 4, 3, 0, 0, 0, 2, 1, 5, 2, 2, 4)
        ),
        Keywords(
            "두바이 초콜릿",
            R.drawable.img_card_dubaichocolate,
            listOf(3, 2, 1, 4, 6, 4, 2, 1, 5, 6, 2, 1, 3, 5)
        ),
        Keywords(
            "까먹는 젤리",
            R.drawable.img_card_jelly,
            listOf(5, 2, 1, 5, 1, 0, 2, 0, 1, 2, 0, 0, 4, 5)
        ),
        Keywords(
            "쇠일러문",
            R.drawable.img_card_metal,
            listOf(0, 0, 0, 0, 0, 2, 4, 5, 5, 4, 5, 4, 2, 5)
        ),
        Keywords(
            "원영적 사고",
            R.drawable.img_card_wonyoung,
            listOf(3, 2, 2, 4, 0, 1, 2, 2, 4, 5, 5, 1, 2, 3)
        ),
    )
}