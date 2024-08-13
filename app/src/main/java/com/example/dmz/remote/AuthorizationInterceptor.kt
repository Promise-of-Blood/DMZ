package com.example.dmz.remote

import com.example.dmz.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url.newBuilder().addQueryParameter(
            "key",
            BuildConfig.YOUTUBE_API_KEY
        ).build()
        val newRequest = chain.request().newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }
}