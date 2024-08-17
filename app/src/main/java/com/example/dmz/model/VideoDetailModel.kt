package com.example.dmz.model

import com.example.dmz.data.model.VideoDetailResponse

data class VideoDetailModel(
    val videoId: String,
    val channelId: String,
    val publishedAt: String,
    val title: String,
    val thumbnail: String,
    val commentCount: String,
    val likeCount: String,
    val viewCount: String
)

fun VideoDetailResponse.toVideoDetail(): VideoDetailModel {
    return this.let {
        VideoDetailModel(
            videoId = it.items[0].id,
            channelId = it.items[0].snippet.channelId,
            publishedAt = it.items[0].snippet.publishedAt,
            title = it.items[0].snippet.title,
            thumbnail = it.items[0].snippet.thumbnails.high.url,
            commentCount = it.items[0].statistics.commentCount,
            likeCount = it.items[0].statistics.likeCount,
            viewCount = it.items[0].statistics.viewCount
        )
    }
}
