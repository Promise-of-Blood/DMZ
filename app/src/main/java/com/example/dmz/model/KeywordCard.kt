package com.example.dmz.model

data class KeywordCard(
    val id: String,
    val thumbnail: Int,
    val keyword: String,
)

fun List<KeywordCard>.include(item: KeywordCard) = this.count { item.keyword == it.keyword } > 0