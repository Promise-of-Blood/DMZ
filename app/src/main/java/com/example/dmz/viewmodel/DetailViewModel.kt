package com.example.dmz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dmz.data.repository.DetailRepository
import com.example.dmz.model.ChannelDetailModel
import com.example.dmz.model.UiState
import com.example.dmz.model.VideoDetailModel
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailRepository) : ViewModel() {
    private val _videoDetail = MutableLiveData<UiState<VideoDetailModel>>()
    val videoDetail: LiveData<UiState<VideoDetailModel>> = _videoDetail

    private val _channelDetail = MutableLiveData<UiState<ChannelDetailModel>>()
    val channelDetail: LiveData<UiState<ChannelDetailModel>> = _channelDetail

    fun fetchDetailData(id: String) {
        _videoDetail.value = UiState.Loading

        viewModelScope.launch {
            try {
                val video = repository.getVideoDetail(id)
                _videoDetail.value = UiState.Success(video)

                val chanel = repository.getChannelDetail(video.channelId)
                _channelDetail.value = UiState.Success(chanel)
            } catch (e: Exception) {
                _videoDetail.value = UiState.Error(e.message ?: "An Error Occurred")
            }
        }
    }
}