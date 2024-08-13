package com.example.dmz.model

import com.example.dmz.data.model.ChannelResponse

data class ChannelModel(
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnail: String,
)

fun ChannelResponse.toChannelList(): List<ChannelModel> {
    return this.items.map {
        ChannelModel(
             channelId= it.id.channelId,
         title= it.snippet.title,
         description= it.snippet.description,
         thumbnail = it.snippet.thumbnails.default.url,
        )
    }
}
