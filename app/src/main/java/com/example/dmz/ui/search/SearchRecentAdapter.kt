package com.example.dmz.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dmz.databinding.ItemRecentSearchBinding
import com.example.dmz.model.SearchEntity

class SearchRecentAdapter(
    private val searchEntityList: List<SearchEntity>
) : RecyclerView.Adapter<SearchRecentAdapter.SearchHolder>() {

    class SearchHolder(val binding: ItemRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchHolder {
        val binding = ItemRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        val searchEntity = searchEntityList[position]
        holder.binding.apply {
            search = searchEntity
        }

        val region = setRegionData(searchEntity.region)
        val sort = setSort(searchEntity.sort)

        holder.binding.apply {
            tvQuery.text = searchEntity.query
            tvRegion.text = region
            tvSort.text = sort
            tvDate.text = searchEntity.date
        }

    }

    override fun getItemCount(): Int {
        return searchEntityList.size
    }

    private fun setRegionData(region: String): String {
        Log.d("SearchViewModel", "setRegionData called with region: $region")
        return when (region) {
            "KR" -> "대한민국"
            "US" -> "미국"
            "JP" -> "일본"
            "GB" -> "영국"
            else -> region
        }
    }

    private fun setSort(input: String): String {
        return when (input) {
            "relevance" -> "관련성 순"
            "date" -> "날짜 순"
            "rating" -> "평점 순"
            "title" -> "제목 순"
            "viewCount" -> "조회수 순"
            else -> input
        }
    }

    private fun setDate(input: String): String {
        return when (input) {
            "relevance" -> "관련성 순"
            "date" -> "날짜 순"
            "rating" -> "평점 순"
            "title" -> "제목 순"
            "viewCount" -> "조회수 순"
            else -> input
        }
    }

}