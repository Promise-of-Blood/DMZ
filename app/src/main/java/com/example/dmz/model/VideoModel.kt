package com.example.dmz.model

import com.example.dmz.data.model.VideoResponse

data class VideoModel(
    val videoId: String,
    val publishedAt: String, // 2022-11-19T08:28:33Z
    val title: String,
    val thumbnail: String,
    val channelTitle: String,
)

fun VideoResponse.toVideoList(): List<VideoModel> {
    return this.items.map {
        VideoModel(
            videoId = it.id.videoId,
            publishedAt = it.snippet.publishedAt,
            title = it.snippet.title,
            thumbnail = it.snippet.thumbnails.default.url,
            channelTitle = it.snippet.channelTitle,
        )
    }
}
