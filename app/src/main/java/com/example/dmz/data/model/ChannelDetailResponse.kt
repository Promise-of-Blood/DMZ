package com.example.dmz.data.model

data class ChannelDetailResponse(
    val etag: String,
    val items: List<ChannelDetailItem>,
    val kind: String,
    val pageInfo: PageInfo,
)

data class ChannelDetailItem(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: ChannelDetailSnippet,
    val statistics: ChannelDetailStatistics
)

data class ChannelDetailSnippet(
    val country: String,
    val customUrl: String,
    val description: String,
    val localized: ChannelDetailLocalized,
    val publishedAt: String,
    val thumbnails: ChannelDetailThumbnails,
    val title: String
)

data class ChannelDetailStatistics(
    val hiddenSubscriberCount: Boolean,
    val subscriberCount: String,
    val videoCount: String,
    val viewCount: String
)

data class ChannelDetailLocalized(
    val description: String,
    val title: String
)

data class ChannelDetailThumbnails(
    val default: ChannelDetailThumbnailSize,
    val high: ChannelDetailThumbnailSize,
    val medium: ChannelDetailThumbnailSize
)

data class ChannelDetailThumbnailSize(
    val height: Int,
    val url: String,
    val width: Int
)