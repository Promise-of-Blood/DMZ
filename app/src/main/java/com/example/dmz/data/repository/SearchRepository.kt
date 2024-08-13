package com.example.dmz.data.repository

import com.example.dmz.model.ChannelModel
import com.example.dmz.model.VideoModel

interface SearchRepository {
    suspend fun searchChannel(
        topicId: String?,

        maxResults: Int?,
        type: String? = "channel",

        regionCode: String? = "KR",
        order: String?,
    ): List<ChannelModel>

    suspend fun searchVideo(
        q: String?,
        topicId: String?,

        maxResults: Int?,
        type: String? = "video",

        regionCode: String? = "KR",
        order: String?,
        publishedAfter: String?,
        publishedBefore: String?,
    ): List<VideoModel>
}
