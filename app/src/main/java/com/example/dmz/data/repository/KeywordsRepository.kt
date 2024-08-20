package com.example.dmz.data.repository

import com.example.dmz.data.model.Keywords
import kotlinx.coroutines.flow.Flow

interface KeywordsRepository {
    fun getKeywordsList() : Flow<List<Keywords>>
}