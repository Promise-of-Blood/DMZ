package com.example.dmz.ui.browse.category


import ItemMarginDecoration
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dmz.R
import com.example.dmz.R.string.browse_life_style
import com.example.dmz.data.repository.SearchRepositoryImpl
import com.example.dmz.databinding.FragmentLifeStyleBinding
import com.example.dmz.remote.YoutubeSearchClient
import com.example.dmz.ui.browse.ChannelListAdapter
import com.example.dmz.ui.browse.VideoListAdapter
import com.example.dmz.ui.browse.fetchBrowseData
import com.example.dmz.ui.browse.initSpinner
import com.example.dmz.ui.browse.loadLastRegion
import com.example.dmz.ui.browse.saveSelectedRegion
import com.example.dmz.utils.Util.wiggle
import com.example.dmz.viewmodel.SearchViewModel
import kotlin.random.Random


class LifeStyleFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    private var _binding : FragmentLifeStyleBinding? = null
    private val binding get() = _binding!!

    private val browseChannelAdapter by lazy { ChannelListAdapter() }
    private val browseVideoAdapter by lazy { VideoListAdapter() }

    private val channelViewModel: SearchViewModel by activityViewModels {
        viewModelFactory { initializer { SearchViewModel(SearchRepositoryImpl(YoutubeSearchClient.youtubeApi)) } }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireContext().getSharedPreferences("regionCode", Context.MODE_PRIVATE)
        _binding = FragmentLifeStyleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val letters = listOf(
            binding.introLayout.lifestyleLetterL,
            binding.introLayout.lifestyleLetterI,
            binding.introLayout.lifestyleLetterF,
            binding.introLayout.lifestyleLetterE,
            binding.introLayout.lifestyleLetterS,
            binding.introLayout.lifestyleLetterT,
            binding.introLayout.lifestyleLetterY,
            binding.introLayout.lifestyleLetterL2,
            binding.introLayout.lifestyleLetterE2
        )

        letters.forEachIndexed{index, view ->
            val duration = Random.nextLong(1000, 3000)
            wiggle(view,duration,0)
        }

        wiggle(binding.introLayout.lifestyle3dFlower,2000,0)

        initBrowseView()
        initBrowseViewModel()

    }

    private fun initBrowseView() = with(binding) {

        val navView: View = requireActivity().findViewById(R.id.nav_view)
        val homeBtn: View = requireActivity().findViewById(R.id.iv_home_btn)
        navView.visibility = View.GONE
        homeBtn.visibility = View.GONE

        mlLifeStyleFragment.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
                Log.d("MotionLayout", "Transition Started: $startId -> $endId")
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Log.d("MotionLayout", "Transition Completed: $currentId")
                if (currentId == R.id.end){
                    navView.visibility = View.VISIBLE
                    homeBtn.visibility = View.VISIBLE
                }else if(currentId == R.id.start){
                    navView.visibility = View.GONE
                    homeBtn.visibility = View.GONE
                }
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })


        listLayout.tvTopbarTitle.text = getString(browse_life_style)
        listLayout.tvChannelTitle.text = getString(browse_life_style)
        listLayout.tvVideoTitle.text = getString(R.string.browse_life_style)

        initSpinner(binding,sharedPreferences)
        listLayout.spinnerSelectRegion.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            val regionCode = when (text) {
                "한국" -> "KR"
                "미국" -> "US"
                "영국" -> "GB"
                "일본" -> "JP"
                else -> "KR"
            }
            saveSelectedRegion(sharedPreferences,regionCode)
            fetchBrowseData(channelViewModel,"/m/019_rr",regionCode)
        }



        listLayout.rvCategoryChannel.apply {
            adapter = browseChannelAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(ItemMarginDecoration(context, 0, 0, 0, 16))
        }

        listLayout.rvCategoryVideo.apply {
            adapter = browseVideoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(ItemMarginDecoration(context, 0, 32, 0, 0))
        }
    }

    private fun initBrowseViewModel() {
        channelViewModel.channelList.observe(viewLifecycleOwner) { channels ->
            browseChannelAdapter.submitList(channels)
        }

        channelViewModel.videoList.observe(viewLifecycleOwner) { videos ->
            browseVideoAdapter.submitList(videos)
        }

        val lastRegionCode = loadLastRegion(sharedPreferences)
        fetchBrowseData(channelViewModel,"/m/019_rr",lastRegionCode)

    }



}