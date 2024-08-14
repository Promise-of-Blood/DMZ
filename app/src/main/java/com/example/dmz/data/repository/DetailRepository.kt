package com.example.dmz.data.repository

import com.example.dmz.model.ChannelDetailModel
import com.example.dmz.model.VideoDetailModel

interface DetailRepository {
    suspend fun getVideoDetail(id: String): VideoDetailModel
    suspend fun getChannelDetail(id: String): ChannelDetailModel
}