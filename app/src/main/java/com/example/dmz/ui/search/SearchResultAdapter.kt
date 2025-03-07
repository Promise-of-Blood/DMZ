package com.example.dmz.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dmz.databinding.ItemSearchResultBinding
import com.example.dmz.model.VideoModel
import com.example.dmz.ui.search.SearchResultAdapter.SearchResultViewHolder
import com.example.dmz.utils.Util.formatDiffDay
import com.example.dmz.utils.Util.formatDiffTime

class SearchResultAdapter : ListAdapter<VideoModel, SearchResultViewHolder>(diffUtil) {

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoModel) {
            binding.apply {
                Glide.with(itemView.context).load(item.thumbnail).into(ivThumbImage)
                tvResultTitle.text = item.title
                tvChannelName.text = item.channelName
                if (item.publishedAt.formatDiffDay() == "0일") {
                    tvVideoTime.text = item.publishedAt.formatDiffTime()
                } else {
                    tvVideoTime.text = String.format("%s 전", item.publishedAt.formatDiffDay())
                }

                Log.d("date", item.publishedAt)
                Log.d("date", item.publishedAt.formatDiffDay())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<VideoModel>() {
            override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                return oldItem.videoId == newItem.videoId
            }

            override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                return oldItem == newItem
            }

        }
    }


}