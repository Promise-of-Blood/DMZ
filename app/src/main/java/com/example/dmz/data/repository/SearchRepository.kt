package com.example.dmz.data.repository

import com.example.dmz.model.ChannelModel
import com.example.dmz.model.VideoModel

interface SearchRepository {
    suspend fun searchChannel(
        topicId: String?,
        maxResults: Int?,
        regionCode: String?,
        order: String?,
    ): List<ChannelModel>

    suspend fun searchVideo(
        q: String?,
        topicId: String?,
        maxResults: Int?,
        regionCode: String?,
        order: String?,
        publishedAfter: String?,
        publishedBefore: String?,
    ): List<VideoModel>
}
