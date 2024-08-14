package com.example.dmz.ui.mypage

enum class MyPageViewType(val viewType: Int) {
    HEADER(0),
    PROFILE(1),
    VIDEO(2),
    CARD_LIST(3),
    DEFAULT(-1),
}

sealed class MyPageListItem {
    data class Header(val title: String, val isMore: Boolean) : MyPageListItem()
    data class Profile(
        val profileImage: Int,
        val name: String,
        val cardCount: Int,
        val gender: String,
        val joinedDate: String
    ) : MyPageListItem()

    data class Video(
        val thumbnail: String,
        val title: String,
        val channelThumbnail: String,
        val channelTitle: String,
        val viewCount: String,
        val publishedDate: String
    ) : MyPageListItem()

    data class CardList(val cards: List<Card>) : MyPageListItem()
    data class Card(val thumbnail: Int, val keyword: String) : MyPageListItem()
    data object Default : MyPageListItem()
}
