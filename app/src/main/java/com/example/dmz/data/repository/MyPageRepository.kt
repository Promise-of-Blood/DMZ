package com.example.dmz.data.repository

import com.example.dmz.model.BookmarkedVideo
import com.example.dmz.model.KeywordCard

interface BookmarkRepository {
    fun addBookmark(item: BookmarkedVideo)
    fun removeBookmark(item: BookmarkedVideo)
    fun isBookmarked(item: BookmarkedVideo): Boolean
    fun saveBookmarkList()
    fun loadBookmarkList(): MutableList<BookmarkedVideo>
}

interface KeywordCardRepository {
    fun addKeywordCard(item: KeywordCard)
    fun removeKeywordCard(item: KeywordCard)
    fun saveKeywordCardList()
    fun loadKeywordCardList(): MutableList<KeywordCard>
}
