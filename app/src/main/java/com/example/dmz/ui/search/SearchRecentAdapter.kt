package com.example.dmz.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dmz.databinding.ItemRecentSearchBinding
import com.example.dmz.model.SearchEntity
import com.example.dmz.utils.Util.koreanToRegionCode
import com.example.dmz.utils.Util.koreanToSortData

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
        val region = koreanToRegionCode(searchEntity.region)
        val sort = koreanToSortData(searchEntity.sort)

        holder.binding.apply {
            tvQuery.text = searchEntity.query
            tvRegion.text = region
            tvSort.text = sort
            tvDate.text = searchEntity.date
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return searchEntityList.size
    }

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

}