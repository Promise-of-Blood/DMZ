package com.example.dmz.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dmz.databinding.ItemMyPageCardBinding

class CardAdapter : ListAdapter<MyPageListItem.Card, CardAdapter.Holder>(object :
    DiffUtil.ItemCallback<MyPageListItem.Card>() {
    override fun areItemsTheSame(
        oldItem: MyPageListItem.Card,
        newItem: MyPageListItem.Card
    ): Boolean {
        return oldItem.thumbnail == newItem.thumbnail
    }

    override fun areContentsTheSame(
        oldItem: MyPageListItem.Card,
        newItem: MyPageListItem.Card
    ): Boolean {
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

        fun bind(item: MyPageListItem.Card) {
            thumbnailImageView.setImageResource(item.thumbnail)
            keywordTextView.text = item.keyword
        }
    }
}