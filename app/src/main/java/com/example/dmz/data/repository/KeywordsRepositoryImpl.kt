package com.example.dmz.data.repository

import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.model.Keywords
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class KeywordsRepositoryImpl(
    private val cacheDataSource: CacheDataSource
) : KeywordsRepository {
    override fun getKeywordsList(): Flow<List<Keywords>> {

        return flow { emit(cacheDataSource.getKeywordsList()) }
    }
}