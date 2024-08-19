package com.example.dmz.data.repository

import com.example.dmz.data.model.Keywords

interface KeywordsRepository {
    fun getKeywordsList() : List<Keywords>
}