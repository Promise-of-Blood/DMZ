package com.example.dmz.data.api

import com.example.dmz.data.model.ChannelResponse
import com.example.dmz.data.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("/youtube/v3/search")
    suspend fun getChannelList(
        @Query("topicId") topicId: String?, // 카테고리
        @Query("order") order: String?, // 정렬 순서, date, rating, relevance, title, videoCount, viewCount
        @Query("regionCode") regionCode: String? = "KR", // 지역 코드
        @Query("maxResults") maxResults: Int? = 5, // 받아올 리스트, default: 5
        @Query("type") type: String? = "channel", // 검색할 미디어, video, channel
        @Query("part") part: String? = "snippet", // snippet 또는 id
    ): ChannelResponse

    @GET("/youtube/v3/search")
    suspend fun getVideoList(
        @Query("q") q: String?, // 검색어
        @Query("topicId") topicId: String?, // 카테고리
        @Query("publishedAfter") publishedAfter: String?, // 지정된 시간 또는 그 이후에 생성된 영상
        @Query("publishedBefore") publishedBefore: String?, // 지정된 시간 또는 그 이전에 생성된 영상
        @Query("order") order: String?, // 정렬 순서, date, rating, relevance, title, videoCount, viewCount
        @Query("regionCode") regionCode: String? = "KR", // 지역 코드
        @Query("maxResults") maxResults: Int? = 5, // 받아올 리스트, default: 5
        @Query("type") type: String? = "video", // 검색할 미디어, video, channel
        @Query("part") part: String? = "snippet", // snippet 또는 id
    ): VideoResponse
}