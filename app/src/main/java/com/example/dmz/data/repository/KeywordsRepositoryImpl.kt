package com.example.dmz.data.repository

import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.model.Keywords

class KeywordsRepositoryImpl(
    private val cacheDataSource: CacheDataSource
) : KeywordsRepository {
    override fun getKeywordsList(): List<Keywords> {
        return cacheDataSource.getUserList()
    }
}