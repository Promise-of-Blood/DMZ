package com.example.dmz.model

import com.example.dmz.R


data class SearchEntity(
    val query: String,
    val region: String,
    val sort: String,
    val date: String,
    val color: Int
)

// 임시데이터
fun listOfSearch(): List<SearchEntity> {
    val searchEntities = mutableListOf<SearchEntity>()

    searchEntities.add(SearchEntity("query1", "region1", "sort1", "date1", R.color.flou_yellow))
    searchEntities.add(SearchEntity("query2", "region2", "sort2", "date2",R.color.pink))
    searchEntities.add(SearchEntity("query3", "region3", "sort3", "date3", R.color.yellow))
    searchEntities.add(SearchEntity("query4", "region4", "sort4", "date4", R.color.green))
    searchEntities.add(SearchEntity("query5", "region5", "sort5", "date5", R.color.sky_blue))
    searchEntities.add(SearchEntity("query6", "region6", "sort6", "date6", R.color.flou_yellow))
    searchEntities.add(SearchEntity("query7", "region7", "sort7", "date7", R.color.pink))

//    search.add(Search("query1", "region1", "sort1", "date1", "#E2FFA9"))
//    search.add(Search("query2", "region2", "sort2", "date2", "#FFC0CB"))
//    search.add(Search("query3", "region3", "sort3", "date3", "#FFFF00"))
//    search.add(Search("query4", "region4", "sort4", "date4", "#008000"))
//    search.add(Search("query5", "region5", "sort5", "date5", "#87CEEB"))
//    search.add(Search("query6", "region6", "sort6", "date6", "#E2FFA9"))
//    search.add(Search("query7", "region7", "sort7", "date7", "#FFC0CB"))

    return searchEntities
}

