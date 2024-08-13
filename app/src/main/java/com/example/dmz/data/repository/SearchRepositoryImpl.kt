package com.example.dmz.data.repository

import com.example.dmz.model.ChannelModel
import com.example.dmz.model.VideoModel
import com.example.dmz.model.toChannelList
import com.example.dmz.model.toVideoList
import com.example.dmz.remote.YoutubeSearchClient.youtubeApi

class SearchRepositoryImpl : SearchRepository {
    override suspend fun searchChannel(
        topicId: String?,
        maxResults: Int?,
        regionCode: String?,
        order: String?
    ): List<ChannelModel> {
        return youtubeApi.getChannelList(
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
        return youtubeApi.getVideoList(
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