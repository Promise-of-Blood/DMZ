package com.example.dmz.ui.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dmz.databinding.CategoryVideoHolderBinding
import com.example.dmz.model.VideoModel

class VideoListAdapter :ListAdapter<VideoModel,RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<VideoModel>(){
    override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
        return oldItem.videoId == newItem.videoId
    }

    override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
        return oldItem == newItem
    }
}){

    class VideoViewHolder(binding : CategoryVideoHolderBinding):RecyclerView.ViewHolder(binding.root){
        private val thumbnail = binding.ivVideoHolderThumbnail
        private val title = binding.tvVideoHolderTitle

        fun bind(item: VideoModel){
            Glide.with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .into(thumbnail)
            title.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CategoryVideoHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoViewHolder).bind(getItem(position))
    }
}