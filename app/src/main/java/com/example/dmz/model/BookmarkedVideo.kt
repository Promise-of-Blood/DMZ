package com.example.dmz.model

data class BookmarkedVideo(
    val video: VideoDetailModel? = null,
    val channel: ChannelDetailModel? = null,
)

fun List<BookmarkedVideo>.include(item: BookmarkedVideo) =
    this.count { item.video?.videoId == it.video?.videoId } > 0