package com.example.dmz.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dmz.databinding.ItemMyPageCardBinding
import com.example.dmz.model.KeywordCard

class CardAdapter : ListAdapter<KeywordCard, CardAdapter.Holder>(object :
    DiffUtil.ItemCallback<KeywordCard>() {
    override fun areItemsTheSame(oldItem: KeywordCard, newItem: KeywordCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: KeywordCard, newItem: KeywordCard): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemMyPageCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(binding: ItemMyPageCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val thumbnailImageView = binding.ivCardImage
        private val keywordTextView = binding.tvCardKeyword

        fun bind(item: KeywordCard) {
            thumbnailImageView.setImageResource(item.thumbnail)
            keywordTextView.text = item.keyword
        }
    }
}