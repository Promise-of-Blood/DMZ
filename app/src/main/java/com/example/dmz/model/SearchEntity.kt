package com.example.dmz.model

data class SearchEntity(
    val query: String,
    val region: String,
    val sort: String,
    val date: String,
    val color: Int
)

