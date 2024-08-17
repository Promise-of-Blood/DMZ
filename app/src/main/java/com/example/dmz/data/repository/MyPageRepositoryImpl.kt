package com.example.dmz.data.repository

import android.content.Context
import com.example.dmz.R
import com.example.dmz.model.BookmarkedVideo
import com.example.dmz.model.KeywordCard
import com.example.dmz.model.MyPageListItem
import com.example.dmz.model.include
import com.example.dmz.model.replaceBookmarkList
import com.example.dmz.model.replaceKeywordCardList
import com.example.dmz.model.setCardCount
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyPageRepositoryImpl(context: Context) : BookmarkRepository, KeywordCardRepository {
    private val gson = Gson()
    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    private val bookmarkPreferenceKey = context.getString(R.string.preference_bookmark_key)
    private val keywordCardPreferenceKey = context.getString(R.string.preference_keyword_card_key)

    private var _bookmarkList = loadBookmarkList()
    val bookmarkList get() = _bookmarkList

    private var _keywordCardList = loadKeywordCardList()
    val keywordCardList get() = _keywordCardList

    private var _myPageData = mutableListOf<MyPageListItem>()
    val myPageData get() = _myPageData

    init {
        _myPageData = mutableListOf(
            MyPageListItem.Profile(cardCount = _keywordCardList.size),
            MyPageListItem.Header("Card collection", false),
            MyPageListItem.KeywordCardList(_keywordCardList),
            MyPageListItem.Header("Bookmark", true),
            MyPageListItem.BookmarkList(_bookmarkList)
        )
    }

    /*
    * Bookmark
    * */
    override fun addBookmark(item: BookmarkedVideo) {
        if (!_bookmarkList.include(item)) {
            _bookmarkList.add(item)
            _myPageData = _myPageData.replaceBookmarkList(list = _bookmarkList).toMutableList()
        }
    }

    override fun removeBookmark(item: BookmarkedVideo) {
        _bookmarkList.removeIf { item.video?.videoId == it.video?.videoId }
        _myPageData = _myPageData.replaceBookmarkList(list = _bookmarkList).toMutableList()
    }

    override fun isBookmarked(item: BookmarkedVideo) = _bookmarkList.include(item)

    override fun saveBookmarkList() {
        val jsonString = gson.toJson(_bookmarkList)
        sharedPreferences.edit().putString(bookmarkPreferenceKey, jsonString).apply()
    }

    override fun loadBookmarkList(): MutableList<BookmarkedVideo> {
        val jsonString =
            sharedPreferences.getString(bookmarkPreferenceKey, null) ?: return mutableListOf()
        val listType = object : TypeToken<MutableList<BookmarkedVideo>>() {}.type
        return gson.fromJson(jsonString, listType)
    }

    /*
    * Keyword Card
    * */
    override fun addKeywordCard(item: KeywordCard) {
        if (!_keywordCardList.include(item)) {
            _keywordCardList.add(item)
            _myPageData =
                _myPageData.replaceKeywordCardList(list = _keywordCardList).toMutableList()
            _myPageData =
                _myPageData.setCardCount(cardCount = _keywordCardList.size).toMutableList()
        }
    }

    override fun removeKeywordCard(item: KeywordCard) {
        _keywordCardList.removeIf { item.id == it.id }
        _myPageData = _myPageData.replaceKeywordCardList(list = _keywordCardList).toMutableList()
        _myPageData = _myPageData.setCardCount(cardCount = _keywordCardList.size).toMutableList()
    }

    override fun saveKeywordCardList() {
        val jsonString = gson.toJson(_keywordCardList)
        sharedPreferences.edit().putString(keywordCardPreferenceKey, jsonString).apply()
    }

    override fun loadKeywordCardList(): MutableList<KeywordCard> {
        val jsonString =
            sharedPreferences.getString(keywordCardPreferenceKey, null) ?: return mutableListOf()
        val listType = object : TypeToken<MutableList<KeywordCard>>() {}.type
        return gson.fromJson(jsonString, listType)
    }
}
