package com.example.dmz.data.repository

import com.example.dmz.data.api.YoutubeApi
import com.example.dmz.model.ChannelDetailModel
import com.example.dmz.model.VideoDetailModel
import com.example.dmz.model.toChannelDetail
import com.example.dmz.model.toVideoDetail

class DetailRepositoryImpl(private val api: YoutubeApi) : DetailRepository {
    override suspend fun getVideoDetail(id: String): VideoDetailModel {
        return api.getVideoDetail(id).toVideoDetail()
    }

    override suspend fun getChannelDetail(id: String): ChannelDetailModel {
        return api.getChannelDetail(id).toChannelDetail()
    }
}