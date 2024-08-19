package com.example.dmz.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.example.dmz.model.KeywordCard
import com.example.dmz.model.MyPageListItem
import com.example.dmz.utils.Util.formatDiffDay
import com.example.dmz.utils.Util.formatDiffTime
import com.example.dmz.utils.Util.formatNumber
import com.example.dmz.utils.Util.px

class MyPageAdapter(
    private val onClickVideo: (item: MyPageListItem, sharedElement: View) -> Unit,
    private val onClickMore: () -> Unit
) : ListAdapter<MyPageListItem, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<MyPageListItem>() {
    override fun areItemsTheSame(oldItem: MyPageListItem, newItem: MyPageListItem): Boolean {
        return when {
            oldItem is MyPageListItem.Header && newItem is MyPageListItem.Header -> oldItem.title == newItem.title
            oldItem is MyPageListItem.Profile && newItem is MyPageListItem.Profile -> oldItem.name == newItem.name
            oldItem is MyPageListItem.Video && newItem is MyPageListItem.Video -> oldItem.hashCode() == newItem.hashCode()
            oldItem is MyPageListItem.KeywordCardList && newItem is MyPageListItem.KeywordCardList -> oldItem.hashCode() == newItem.hashCode()
            oldItem is MyPageListItem.Empty && newItem is MyPageListItem.Empty -> oldItem.hashCode() == newItem.hashCode()
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: MyPageListItem, newItem: MyPageListItem): Boolean {
        return oldItem == newItem
    }
}) {
    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_PROFILE = 1
        private const val TYPE_VIDEO = 2
        private const val TYPE_CARD = 3
        private const val TYPE_EMPTY = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> EmptyHolder(parent.context)

            TYPE_HEADER -> HeaderHolder(
                ItemMyPageHeaderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            TYPE_PROFILE -> ProfileHolder(
                ItemMyPageProfileBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            TYPE_VIDEO -> VideoHolder(
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
            is MyPageListItem.Empty -> TYPE_EMPTY
            is MyPageListItem.Header -> TYPE_HEADER
            is MyPageListItem.Profile -> TYPE_PROFILE
            is MyPageListItem.Video -> TYPE_VIDEO
            else -> TYPE_CARD
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyHolder -> holder.bind(getItem(position))
            is HeaderHolder -> holder.bind(getItem(position), onClickMore)
            is ProfileHolder -> holder.bind(getItem(position))
            is VideoHolder -> holder.bind(getItem(position), onClickVideo)
            else -> {
                val item = getItem(position) as MyPageListItem.KeywordCardList
                (holder as CardHolder).bind(item.list)
            }
        }
    }

    class EmptyHolder(context: Context) : RecyclerView.ViewHolder(TextView(context)) {
        fun bind(item: MyPageListItem) {
            (item as MyPageListItem.Empty).let {
                (itemView as TextView).apply {
                    text = it.message
                    setTextAppearance(R.style.description_p)
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    setTextColor(itemView.context.getColor(R.color.light_gray))
                    setPadding(0, 40f.px, 0, 40f.px)
                    layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                }
            }
        }
    }

    class HeaderHolder(binding: ItemMyPageHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        private val titleTextView = binding.tvHeaderTitle
        private val moreTextView = binding.tvHeaderMore

        fun bind(item: MyPageListItem, onClick: () -> Unit) {
            (item as MyPageListItem.Header).let {
                titleTextView.text = it.title
                moreTextView.visibility = if (it.isMore) View.VISIBLE else View.GONE
                moreTextView.setOnClickListener { onClick() }
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

        fun bind(
            item: MyPageListItem,
            onClick: (item: MyPageListItem, sharedElement: View) -> Unit
        ) {
            (item as MyPageListItem.Video).item.let {
                Glide.with(itemView.context).load(it.video?.thumbnail)
                    .into(thumbnailImageView)
                thumbnailImageView.transitionName = "thumbnail_${it.video?.videoId}"
                titleTextView.text = it.video?.title
                viewCountTextView.text = itemView.context.getString(
                    R.string.my_page_video_view_count,
                    it.video?.viewCount?.formatNumber()
                )
                publishedDateTextView.text = it.video?.publishedAt?.formatDiffTime()
                Glide.with(itemView.context).load(it.channel?.thumbnail).centerInside()
                    .into(channelThumbnailImageView)
                channelTitleTextView.text = it.channel?.title
                itemView.setOnClickListener { onClick(item, thumbnailImageView) }
            }
        }
    }

    class CardHolder(binding: ItemMyPageCardListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val root = binding.root
        private val cardListRecyclerView = binding.rvCardList

        fun bind(item: List<KeywordCard>) = with(cardListRecyclerView) {
            if (item.isEmpty()) {
                cardListRecyclerView.visibility = View.GONE
                val emptyHolder = EmptyHolder(context)
                emptyHolder.bind(MyPageListItem.Empty("아직 수집한 카드가 없어요.\n퀴즈를 풀고 카드를 모아보세요!"))
                root.addView(emptyHolder.itemView)
                return@with
            }
            cardListRecyclerView.visibility = View.GONE
            val cardAdapter = CardAdapter().apply { submitList(item) }
            adapter = cardAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = null
            cardListRecyclerView.visibility = View.VISIBLE
        }
    }
}
