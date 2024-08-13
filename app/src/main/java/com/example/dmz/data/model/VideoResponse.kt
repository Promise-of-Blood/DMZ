package com.example.dmz.data.model

data class VideoResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val regionCode: String,
    val pageInfo: PageInfo,
    val items: List<VideoItem>,
)

data class VideoItem(
    val kind: String,
    val etag: String,
    val id: VideoId,
    val snippet: VideoSnippet,
)

data class VideoId(
    val kind: String,
    val videoId: String,
)

data class VideoSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: VideoThumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String,
)

data class VideoThumbnails(
    val default: VideoThumbnailSize,
    val high: VideoThumbnailSize,
    val medium: VideoThumbnailSize,
)

data class VideoThumbnailSize(
    val height: Int,
    val url: String,
    val width: Int
)
