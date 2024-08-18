package com.example.dmz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dmz.data.repository.MyPageRepositoryImpl
import com.example.dmz.model.BookmarkedVideo
import com.example.dmz.model.KeywordCard
import com.example.dmz.model.MyPageListItem

class MyPageViewModel(private val repository: MyPageRepositoryImpl) : ViewModel() {
    private val _bookmarkList = MutableLiveData<List<BookmarkedVideo>>()
    val bookmarkList: LiveData<List<BookmarkedVideo>> = _bookmarkList

    private val _keywordCardList = MutableLiveData<List<KeywordCard>>()
    val keywordCardList: LiveData<List<KeywordCard>> = _keywordCardList

    private var _myPageData = MutableLiveData<List<MyPageListItem>>()
    val myPageData: LiveData<List<MyPageListItem>> = _myPageData

    init {
        loadBookmarkList()
        loadKeywordCardList()
    }

    /*
    * Bookmark
    * */
    private fun loadBookmarkList() {
        _bookmarkList.value = repository.bookmarkList
        _myPageData.value = repository.myPageData
    }

    fun addBookmark(item: BookmarkedVideo) {
        repository.addBookmark(item)
        loadBookmarkList()
    }

    fun removeBookmark(item: BookmarkedVideo) {
        repository.removeBookmark(item)
        loadBookmarkList()
    }

    fun isBookmarked(item: BookmarkedVideo) = repository.isBookmarked(item)

    /*
    * Keyword Card
    * */
    private fun loadKeywordCardList() {
        _keywordCardList.value = repository.keywordCardList
        _myPageData.value = repository.myPageData
    }

    fun addKeywordCard(item: KeywordCard) {
        repository.addKeywordCard(item)
        loadKeywordCardList()
    }

    fun removeKeywordCard(item: KeywordCard) {
        repository.removeKeywordCard(item)
        loadKeywordCardList()
    }

    /*
    * data backup
    * */
    fun saveData() {
        repository.saveBookmarkList()
        repository.saveKeywordCardList()
    }
}
