package com.example.dmz.data.model

data class VideoDetailResponse(
    val kind: String,
    val etag: String,
    val pageInfo: PageInfo,
    val items: List<VideoDetailItem>,
)

data class VideoDetailItem(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: VideoDetailSnippet,
    val statistics: VideoDetailStatistics
)

data class VideoDetailSnippet(
    val categoryId: String,
    val channelId: String,
    val channelTitle: String,
    val defaultAudioLanguage: String,
    val description: String,
    val liveBroadcastContent: String,
    val localized: VideoDetailLocalized,
    val publishedAt: String,
    val tags: List<String>,
    val thumbnails: VideoDetailThumbnails,
    val title: String
)

data class VideoDetailStatistics(
    val commentCount: String,
    val favoriteCount: String,
    val likeCount: String,
    val viewCount: String
)

data class VideoDetailLocalized(
    val description: String,
    val title: String
)

data class VideoDetailThumbnails(
    val default: VideoDetailThumbnailSize,
    val high: VideoDetailThumbnailSize,
    val maxres: VideoDetailThumbnailSize,
    val medium: VideoDetailThumbnailSize,
    val standard: VideoDetailThumbnailSize
)

data class VideoDetailThumbnailSize(
    val height: Int,
    val url: String,
    val width: Int
)