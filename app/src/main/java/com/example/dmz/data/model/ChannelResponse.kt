package com.example.dmz.data.model

data class ChannelResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val regionCode: String,
    val pageInfo: PageInfo,
    val items: List<ChannelItem>,
)

data class PageInfo(
    val resultsPerPage: Int,
    val totalResults: Int,
)

data class ChannelItem(
    val kind: String,
    val etag: String,
    val id: ChannelId,
    val snippet: ChannelSnippet,
)

data class ChannelId(
    val kind: String,
    val channelId: String,
)

data class ChannelSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: ChannelThumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String,
)

data class ChannelThumbnails(
    val default: ChannelThumbnailSize,
    val high: ChannelThumbnailSize,
    val medium: ChannelThumbnailSize,
)

data class ChannelThumbnailSize(
    val url: String,
)
