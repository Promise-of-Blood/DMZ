package com.example.dmz.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dmz.R
import com.example.dmz.databinding.ItemMyPageCardListBinding
import com.example.dmz.databinding.ItemMyPageHeaderBinding
import com.example.dmz.databinding.ItemMyPageProfileBinding
import com.example.dmz.databinding.ItemMyPageVideoBinding
import com.example.dmz.utils.Util.formatDiffDay
import com.example.dmz.utils.Util.formatDiffTime
import com.example.dmz.utils.Util.formatNumber

class MyPageAdapter : ListAdapter<MyPageListItem, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<MyPageListItem>() {
    override fun areItemsTheSame(oldItem: MyPageListItem, newItem: MyPageListItem): Boolean {
        return when {
            oldItem is MyPageListItem.Header && newItem is MyPageListItem.Header -> oldItem.title == newItem.title
            oldItem is MyPageListItem.Profile && newItem is MyPageListItem.Profile -> oldItem.name == newItem.name
            oldItem is MyPageListItem.Video && newItem is MyPageListItem.Video -> oldItem.thumbnail == newItem.title
            oldItem is MyPageListItem.Card && newItem is MyPageListItem.Card -> oldItem.thumbnail == newItem.thumbnail
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: MyPageListItem, newItem: MyPageListItem): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val muiltiViewType = MyPageViewType.entries.find { it.viewType == viewType }
        return when (muiltiViewType) {
            MyPageViewType.HEADER -> HeaderHolder(
                ItemMyPageHeaderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            MyPageViewType.PROFILE -> ProfileHolder(
                ItemMyPageProfileBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            MyPageViewType.VIDEO -> VideoHolder(
                ItemMyPageVideoBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            else -> CardHolder(
                ItemMyPageCardListBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyPageListItem.Header -> MyPageViewType.HEADER.viewType
            is MyPageListItem.Profile -> MyPageViewType.PROFILE.viewType
            is MyPageListItem.Video -> MyPageViewType.VIDEO.viewType
            else -> MyPageViewType.CARD_LIST.viewType
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderHolder -> holder.bind(getItem(position))
            is ProfileHolder -> holder.bind(getItem(position))
            is VideoHolder -> holder.bind(getItem(position))
            else -> {
                val item = getItem(position) as MyPageListItem.CardList
                (holder as CardHolder).bind(item.cards)
            }
        }
    }

    class HeaderHolder(binding: ItemMyPageHeaderBinding) :RecyclerView.ViewHolder(binding.root) {
        private val titleTextView = binding.tvHeaderTitle
        private val moreTextView = binding.tvHeaderMore

        fun bind(item: MyPageListItem) {
            (item as MyPageListItem.Header).let {
                titleTextView.text = it.title
                moreTextView.visibility = if (it.isMore) View.VISIBLE else View.GONE
            }
        }
    }

    class ProfileHolder(binding: ItemMyPageProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        private val profileImageView = binding.ivProfileImage
        private val nameTextView = binding.tvProfileName
        private val cardCountTextView = binding.tvProfileCardCount
        private val genderTextView = binding.tvProfileGender
        private val joinedDateTextView = binding.tvProfileJoinedDate

        fun bind(item: MyPageListItem) {
            (item as MyPageListItem.Profile).let {
                Glide.with(itemView.context)
                    .load(it.profileImage)
                    .centerInside()
                    .transform(RoundedCorners(30))
                    .into(profileImageView)
                profileImageView.setImageResource(it.profileImage)
                nameTextView.text = it.name
                cardCountTextView.text = it.cardCount.toString()
                genderTextView.text = it.gender
                joinedDateTextView.text = it.joinedDate.formatDiffDay()
            }
        }
    }

    class VideoHolder(binding: ItemMyPageVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        private val thumbnailImageView = binding.ivVideoThumbnail
        private val titleTextView = binding.tvVideoTitle
        private val channelThumbnailImageView = binding.ivVideoChannelThumbnail
        private val channelTitleTextView = binding.tvVideoChannelTitle
        private val viewCountTextView = binding.tvVideoViewCount
        private val publishedDateTextView = binding.tvVideoPublishedDate

        fun bind(item: MyPageListItem) {
            (item as MyPageListItem.Video).let {
                Glide.with(itemView.context).load(it.thumbnail).centerInside()
                    .into(thumbnailImageView)
                titleTextView.text = it.title
                Glide.with(itemView.context).load(it.channelThumbnail).centerInside()
                    .into(channelThumbnailImageView)
                channelTitleTextView.text = it.channelTitle
                viewCountTextView.text = itemView.context.getString(R.string.my_page_video_view_count, it.viewCount.formatNumber())
                publishedDateTextView.text = it.publishedDate.formatDiffTime()
            }
        }
    }

    class CardHolder(binding: ItemMyPageCardListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val cardListRecyclerView = binding.rvCardList

        fun bind(item: List<MyPageListItem.Card>) = with(cardListRecyclerView) {
            val cardAdapter = CardAdapter().apply { submitList(item) }
            adapter = cardAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
