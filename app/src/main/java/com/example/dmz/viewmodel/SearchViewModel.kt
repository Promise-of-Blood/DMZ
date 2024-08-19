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

    fun getRecentSearchItems(context: Context){
        _recentSearchedList.value = Util.getPrefRecentSearchList(context)
    }


    fun addRecentSearch(searchEntity: SearchEntity) {
        val search = searchEntity
//            SearchEntity(query = query, region = region, sort = sort, date = date, color = color)
        val searchList = recentSearchedList.value
        searchList?.let {
            val updateList = it.toMutableList()
            updateList.add(searchEntity)

            _recentSearchedList.value = updateList

        }
    }

    fun addSharedPrf(context: Context, searchEntity: SearchEntity) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        ) ?: return
        with(sharedPref.edit()){
//            putString(context.getString(R.string.preference_recent_search_key), searchEntity)
            apply()
        }
    }


    class SearchViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return SearchViewModel(SearchRepositoryImpl(YoutubeSearchClient.youtubeApi)) as T
        }
    }

}