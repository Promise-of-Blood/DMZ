package com.example.dmz.model

import com.example.dmz.R

sealed class MyPageListItem {
    data object Default : MyPageListItem()
    data class Header(val title: String, val isMore: Boolean) : MyPageListItem()
    data class Profile(
        val name: String = "김태영",
        val profileImage: Int = R.drawable.ic_mypage, // image resource id
        val gender: String = "여",
        val joinedDate: String = "2024-08-01T16:44:44Z", // ISO 8601, yyyy-MM-ddTHH:mm:ssZ
        val cardCount: Int = 0,
    ) : MyPageListItem()

    data class KeywordCardList(
        val list: List<KeywordCard> = listOf() // 모은 카드 목록
    ) : MyPageListItem()

    data class Video(
        val item: BookmarkedVideo,
    ) : MyPageListItem()
}

fun MutableList<MyPageListItem>.setBookmarkList(list: List<BookmarkedVideo>) {
    this.apply {
        removeAll { it is MyPageListItem.Video }
        addAll(list.map { MyPageListItem.Video(it) })
    }
}

fun List<MyPageListItem>.replaceKeywordCardList(list: List<KeywordCard>) = this.map {
    when (it) {
        is MyPageListItem.KeywordCardList -> it.copy(list = list)
        else -> it
    }
}

fun List<MyPageListItem>.setCardCount(cardCount: Int) = this.map {
    when (it) {
        is MyPageListItem.Profile -> it.copy(cardCount = cardCount)
        else -> it
    }
}
