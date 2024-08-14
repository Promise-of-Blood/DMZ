package com.example.dmz.model

data class Search(
    val query: String,
    val region: String,
    val sort: String,
    val date: String
)

// 임시데이터
fun listOfSearch(): List<Search> {
    val search = mutableListOf<Search>()

    search.add(Search("query1", "region1", "sort1", "date1"))
    search.add(Search("query2", "region2", "sort2", "date2"))
    search.add(Search("query3", "region3", "sort3", "date3"))
    search.add(Search("query4", "region4", "sort4", "date4"))
    search.add(Search("query5", "region5", "sort5", "date5"))
    search.add(Search("query6", "region6", "sort6", "date6"))
    search.add(Search("query7", "region7", "sort7", "date7"))

    return search
}