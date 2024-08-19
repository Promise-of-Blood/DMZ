package com.example.dmz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dmz.data.repository.SearchRepository
import com.example.dmz.data.repository.SearchRepositoryImpl
import com.example.dmz.model.ChannelModel
import com.example.dmz.model.VideoModel
import com.example.dmz.remote.YoutubeSearchClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    private val _channelList = MutableLiveData<List<ChannelModel>>()
    val channelList: LiveData<List<ChannelModel>> = _channelList

    private val _videoList = MutableLiveData<List<VideoModel>>()
    val videoList: LiveData<List<VideoModel>> = _videoList

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

    fun getVideoList(
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

    class SearchViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return SearchViewModel(SearchRepositoryImpl(YoutubeSearchClient.youtubeApi)) as T
        }
    }

}