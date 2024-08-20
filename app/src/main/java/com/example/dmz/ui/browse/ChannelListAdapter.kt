package com.example.dmz.ui.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dmz.databinding.CategoryChannelHolderBinding
import com.example.dmz.model.ChannelDetailModel
import com.example.dmz.model.ChannelModel

class ChannelListAdapter : ListAdapter<ChannelModel,RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<ChannelModel>(){
    override fun areItemsTheSame(oldItem: ChannelModel, newItem: ChannelModel): Boolean {
        return oldItem.channelId == newItem.channelId
    }

    override fun areContentsTheSame(oldItem: ChannelModel, newItem: ChannelModel): Boolean {
        return oldItem == newItem
    }
}) {
    class ChannelViewHolder(binding: CategoryChannelHolderBinding):RecyclerView.ViewHolder(binding.root){
        private val thumbnail = binding.ivChannelHolderThumbnail
        private val title = binding.tvChannelHolderTitle
        private val description = binding.tvChannelHolderDesc

        fun bind(item: ChannelModel){
            Glide.with(itemView.context).load(item.thumbnail).into(thumbnail)
            title.text = item.title
            description.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CategoryChannelHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChannelViewHolder).bind(getItem(position))
    }

}