package com.example.dmz.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dmz.DMZApplication
import com.example.dmz.R
import com.example.dmz.data.repository.DetailRepositoryImpl
import com.example.dmz.data.repository.MyPageRepositoryImpl
import com.example.dmz.databinding.FragmentDetailBinding
import com.example.dmz.model.BookmarkedVideo
import com.example.dmz.model.ChannelDetailModel
import com.example.dmz.model.UiState
import com.example.dmz.model.VideoDetailModel
import com.example.dmz.remote.YoutubeSearchClient
import com.example.dmz.utils.Util.formatDate
import com.example.dmz.utils.Util.formatNumber
import com.example.dmz.viewmodel.DetailViewModel
import com.example.dmz.viewmodel.MyPageViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var detailData = BookmarkedVideo()
    private val detailViewModel: DetailViewModel by viewModels {
        viewModelFactory { initializer { DetailViewModel(DetailRepositoryImpl(YoutubeSearchClient.youtubeApi)) } }
    }
    private val myPageViewModel: MyPageViewModel by activityViewModels {
        viewModelFactory { initializer { MyPageViewModel(MyPageRepositoryImpl(requireActivity().application as DMZApplication)) } }
    }

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBottomNavigationVisibility(false)
        initView()
        initViewModel()
        detailViewModel.fetchDetailData(args.videoId)
    }

    override fun onDestroy() {
        super.onDestroy()
        handleBottomNavigationVisibility(true)
        _binding = null
    }

    private fun initView() = with(binding) {
        pbDetailLoading.visibility = View.GONE
        svDetailContent.visibility = View.VISIBLE
        tvDetailBookmarkButton.setOnClickListener {
            if (detailData.video != null && detailData.channel != null) {
                if (myPageViewModel.isBookmarked(detailData)) {
                    myPageViewModel.removeBookmark(detailData)
                    tvDetailBookmarkButton.text = getString(R.string.detail_bookmark_button_save)
                    Toast.makeText(requireContext(), "북마크가 취소되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    myPageViewModel.addBookmark(detailData)
                    tvDetailBookmarkButton.text = getString(R.string.detail_bookmark_button_cancel)
                    Toast.makeText(requireContext(), "북마크에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleBottomNavigationVisibility(isShow: Boolean) {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility =
            if (isShow) View.VISIBLE else View.GONE
        activity?.findViewById<ImageView>(R.id.iv_home_btn)?.visibility =
            if (isShow) View.VISIBLE else View.GONE
    }

    private fun initVideoDetailData(video: VideoDetailModel) =
        with(binding) {
            tvDetailVideoTitle.text = video.title
            tvDetailVideoLikeCount.text = video.likeCount.formatNumber()
            tvDetailVideoViewCount.text =
                requireContext().getString(
                    R.string.detail_num_of_times,
                    video.viewCount.formatNumber()
                )
            tvDetailVideoCommentCount.text =
                requireContext().getString(
                    R.string.detail_num_of_counts,
                    video.commentCount.formatNumber()
                )
            tvDetailVideoPublishedDate.text = video.publishedAt.formatDate()
            Glide.with(requireContext())
                .load(video.thumbnail)
                .into(ivDetailVideoThumbnail)
            tvDetailBookmarkButton.text =
                if (myPageViewModel.isBookmarked(detailData)) "북마크 취소" else "북마크 저장"
        }

    private fun initChannelDetailData(channel: ChannelDetailModel) =
        with(binding) {
            tvDetailChannelTitle.text = channel.title
            tvDetailChannelVideoCount.text =
                requireContext().getString(
                    R.string.detail_num_of_counts,
                    channel.videoCount.formatNumber()
                )
            tvDetailChannelSubscriberCount.text =
                requireContext().getString(
                    R.string.detail_num_of_people,
                    channel.subscriberCount.formatNumber()
                )
            Glide.with(requireContext())
                .load(channel.thumbnail)
                .into(ivDetailChannelThumbnail)
        }

    private fun initViewModel() = with(detailViewModel) {
        videoDetail.observe(viewLifecycleOwner) { video ->
            when (video) {
                is UiState.Loading -> {}
                is UiState.Error -> Toast.makeText(requireContext(), video.message, Toast.LENGTH_SHORT).show()
                is UiState.Success -> {
                    detailData = detailData.copy(video = video.data)
                    initVideoDetailData(video.data)
                }
            }
        }
        channelDetail.observe(viewLifecycleOwner) { channel ->
            when (channel) {
                is UiState.Loading -> {}
                is UiState.Error -> Toast.makeText(requireContext(), channel.message, Toast.LENGTH_SHORT).show()
                is UiState.Success -> {
                    detailData = detailData.copy(channel = channel.data)
                    initChannelDetailData(channel.data)
                }
            }
        }
    }
}