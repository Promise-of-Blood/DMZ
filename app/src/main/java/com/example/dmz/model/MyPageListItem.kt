package com.example.dmz.model

import com.example.dmz.R
import com.example.dmz.utils.Util.toISO8601
import java.time.LocalDateTime

sealed class MyPageListItem {
    data class Empty(val message: String) : MyPageListItem()
    data class Header(val title: String, val isMore: Boolean) : MyPageListItem()
    data class Profile(
        val name: String = "김태영",
        val profileImage: Int = R.drawable.ic_mypage, // image resource id
        val gender: String = "여",
        val joinedDate: String = LocalDateTime.of(2002, 2, 2, 0, 0).toISO8601(),
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
        removeAll { it is MyPageListItem.Video || it is MyPageListItem.Empty }
        if (list.isEmpty()) add(MyPageListItem.Empty("아직 저장한 영상이 없어요."))
        else addAll(list.map { MyPageListItem.Video(it) })
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
