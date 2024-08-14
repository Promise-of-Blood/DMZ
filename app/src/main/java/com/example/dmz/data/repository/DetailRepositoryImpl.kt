package com.example.dmz.data.repository

import com.example.dmz.model.ChannelDetailModel
import com.example.dmz.model.VideoDetailModel
import com.example.dmz.model.toChannelDetail
import com.example.dmz.model.toVideoDetail
import com.example.dmz.remote.YoutubeSearchClient.youtubeApi

class DetailRepositoryImpl : DetailRepository {
    override suspend fun getVideoDetail(id: String): VideoDetailModel {
        return youtubeApi.getVideoDetail(id).toVideoDetail()
    }

    override suspend fun getChannelDetail(id: String): ChannelDetailModel {
        return youtubeApi.getChannelDetail(id).toChannelDetail()
    }
}