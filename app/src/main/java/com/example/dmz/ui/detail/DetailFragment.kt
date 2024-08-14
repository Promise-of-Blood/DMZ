package com.example.dmz.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide
import com.example.dmz.R
import com.example.dmz.data.repository.DetailRepositoryImpl
import com.example.dmz.databinding.FragmentDetailBinding
import com.example.dmz.model.ChannelDetailModel
import com.example.dmz.model.UiState
import com.example.dmz.model.VideoDetailModel
import com.example.dmz.viewmodel.DetailViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val VIDEO_ID = "CcGKBi1pm7Y"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels {
        viewModelFactory { initializer { DetailViewModel(DetailRepositoryImpl()) } }
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
        initViewModel()
        detailViewModel.fetchDetailData(VIDEO_ID)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun hideBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        activity?.findViewById<ImageView>(R.id.iv_home_btn)?.visibility = View.GONE
    }

    private fun initVideoDetailData(video: VideoDetailModel) =
        with(binding) {
            tvDetailVideoTitle.text = video.title
            tvDetailVideoLikeCount.text = video.likeCount
            tvDetailVideoViewCount.text =
                requireContext().getString(R.string.detail_num_of_times, video.viewCount)
            tvDetailVideoCommentCount.text =
                requireContext().getString(R.string.detail_num_of_counts, video.commentCount)
            tvDetailVideoPublishedDate.text = video.publishedAt
            Glide.with(requireContext())
                .load(video.thumbnail)
                .into(ivDetailVideoThumbnail)
        }

    private fun initChannelDetailData(channel: ChannelDetailModel) = with(binding) {
        tvDetailChannelTitle.text = channel.title
        tvDetailChannelVideoCount.text =
            requireContext().getString(R.string.detail_num_of_counts, channel.videoCount)
        tvDetailChannelSubscriberCount.text =
            requireContext().getString(R.string.detail_num_of_people, channel.subscriberCount)
        Glide.with(requireContext())
            .load(channel.thumbnail)
            .into(ivDetailChannelThumbnail)
    }

    private fun initViewModel() = with(detailViewModel) {
        videoDetail.observe(viewLifecycleOwner) { video ->
            if (video is UiState.Success) {
                initVideoDetailData(video.data)
            }
        }
        channelDetail.observe(viewLifecycleOwner) { channel ->
            if (channel is UiState.Success) {
                initChannelDetailData(channel.data)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}