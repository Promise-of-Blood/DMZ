package com.example.dmz.data.repository

import com.example.dmz.data.api.YoutubeApi
import com.example.dmz.model.ChannelModel
import com.example.dmz.model.VideoModel
import com.example.dmz.model.toChannelList
import com.example.dmz.model.toVideoList

class SearchRepositoryImpl(private val api: YoutubeApi) : SearchRepository {
    override suspend fun searchChannel(
        topicId: String?,
        maxResults: Int?,
        regionCode: String?,
        order: String?
    ): List<ChannelModel> {
        return api.getChannelList(
            topicId = topicId,
            maxResults = maxResults,
            regionCode = regionCode,
            order = order,
        ).toChannelList()
    }

    override suspend fun searchVideo(
        q: String?,
        topicId: String?,
        maxResults: Int?,
        regionCode: String?,
        order: String?,
        publishedAfter: String?,
        publishedBefore: String?
    ): List<VideoModel> {
        return api.getVideoList(
            q = q,
            topicId = topicId,
            maxResults = maxResults,
            regionCode = regionCode,
            order = order,
            publishedAfter = publishedAfter,
            publishedBefore = publishedBefore,
        ).toVideoList()
    }
}