package com.example.dmz.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dmz.R
import com.example.dmz.databinding.ItemRecentSearchBinding
import com.example.dmz.model.SearchEntity

class SearchRecentAdapter(
    private val searchEntityList: List<SearchEntity>
) : RecyclerView.Adapter<SearchRecentAdapter.SearchHolder>() {
    class SearchHolder(val itemLayoutBinding: ItemRecentSearchBinding) :
        RecyclerView.ViewHolder(itemLayoutBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchHolder {
        val itemLayoutBinding = DataBindingUtil.inflate<ItemRecentSearchBinding>(
            LayoutInflater.from(parent.context), R.layout.item_recent_search, parent, false
        )
        return SearchHolder(itemLayoutBinding)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.itemLayoutBinding.search = searchEntityList[position]
    }

    override fun getItemCount(): Int {
        return searchEntityList.size
    }


}