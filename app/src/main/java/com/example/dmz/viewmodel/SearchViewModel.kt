package com.example.dmz.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dmz.R
import com.example.dmz.data.repository.SearchRepository
import com.example.dmz.data.repository.SearchRepositoryImpl
import com.example.dmz.model.ChannelModel
import com.example.dmz.model.SearchEntity
import com.example.dmz.model.VideoModel
import com.example.dmz.remote.YoutubeSearchClient
import com.example.dmz.utils.Util
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    private val _channelList = MutableLiveData<List<ChannelModel>>()
    val channelList: LiveData<List<ChannelModel>> = _channelList

    private val _videoList = MutableLiveData<List<VideoModel>>()
    val videoList: LiveData<List<VideoModel>> = _videoList

    private val _recentSearchedList = MutableLiveData<List<SearchEntity>>()
    val recentSearchedList: LiveData<List<SearchEntity>> = _recentSearchedList

    fun getChannelList(
        topicId: String? = null,
        maxResults: Int? = null,
        order: String? = null,
        regionCode: String? = "KR",
    ) {
        viewModelScope.launch {
            val channelList =
                async {
                    searchRepository.searchChannel(
                        topicId = topicId,
                        maxResults = maxResults,
                        regionCode = regionCode,
                        order = order,
                    )
                }

            _channelList.value = channelList.await()
        }
    }

    fun doVideoSearch(
        q: String? = null,
        topicId: String? = null,
        maxResults: Int? = null,
        order: String? = null,
        publishedAfter: String? = null,
        publishedBefore: String? = null,
        regionCode: String? = "KR",
    ) {
        viewModelScope.launch {
            val videoList = async {
                searchRepository.searchVideo(
                    q = q,
                    topicId = topicId,
                    maxResults = maxResults,
                    order = order,
                    publishedAfter = publishedAfter,
                    publishedBefore = publishedBefore,
                    regionCode = regionCode,
                )
            }
            _videoList.value = videoList.await()
        }
    }

    fun saveRecentSearchList(context: Context) {
        val gson = Gson()
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        val jsonString = gson.toJson(recentSearchedList.value)
        sharedPreferences.edit()
            .putString(context.getString(R.string.preference_recent_search_key), jsonString).apply()
    }

    fun loadRecentSearchItems(context: Context) {
        val gson = Gson()
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val jsonString =
            sharedPreferences.getString(
                context.getString(R.string.preference_recent_search_key),
                "[]"
            )
        val listType = object : TypeToken<MutableList<SearchEntity>>() {}.type
        _recentSearchedList.value = gson.fromJson(jsonString, listType)
    }

    fun addRecentSearch(searchEntity: SearchEntity) {
        val searchList = recentSearchedList.value
        searchList?.let {
            val updateList = it.toMutableList()
            updateList.add(0, searchEntity)
            _recentSearchedList.value = updateList
        }
    }


    class SearchViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return SearchViewModel(SearchRepositoryImpl(YoutubeSearchClient.youtubeApi)) as T
        }
    }

}