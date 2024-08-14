package com.example.dmz.model

import com.example.dmz.data.model.ChannelDetailResponse
import com.example.dmz.utils.Util.formatNumber

data class ChannelDetailModel(
    val channelId: String,
    val title: String,
    val thumbnail: String,
    val subscriberCount: String,
    val videoCount: String,
)

fun ChannelDetailResponse.toChannelDetail(): ChannelDetailModel {
    return this.let {
        ChannelDetailModel(
            channelId = it.items[0].id,
            title = it.items[0].snippet.title,
            thumbnail = it.items[0].snippet.thumbnails.high.url,
            subscriberCount = it.items[0].statistics.subscriberCount.formatNumber(),
            videoCount = it.items[0].statistics.videoCount.formatNumber(),
        )
    }
}