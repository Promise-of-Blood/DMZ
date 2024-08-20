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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.example.dmz.ui.browse.bottomNavControl
import com.example.dmz.ui.browse.fetchBrowseData
import com.example.dmz.ui.browse.initSpinner
import com.example.dmz.ui.browse.loadLastRegion
import com.example.dmz.ui.browse.saveSelectedRegion
import com.example.dmz.ui.browse.setScrollSensitivity
import com.example.dmz.utils.Util.wiggle
import com.example.dmz.viewmodel.BrowseMotionViewModel
import com.example.dmz.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class LifeStyleFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences
    private var previousRegionCode : String? = null

    private var _binding : FragmentLifeStyleBinding? = null
    private val binding get() = _binding!!

    private val browseChannelAdapter by lazy { ChannelListAdapter() }
    private val browseVideoAdapter by lazy { VideoListAdapter() }

    private val channelViewModel: SearchViewModel by viewModels {
        viewModelFactory { initializer { SearchViewModel(SearchRepositoryImpl(YoutubeSearchClient.youtubeApi)) } }
    }

    private val motionViewModel: BrowseMotionViewModel by activityViewModels {
        viewModelFactory { initializer { BrowseMotionViewModel() } }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        letters.forEachIndexed{_, view ->
            val duration = Random.nextLong(1000, 3000)
            wiggle(view,duration,0)
        }

        wiggle(binding.introLayout.lifestyle3dFlower,2000,0)

        initBrowseView()
        initBrowseViewModel()

    }

    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        Log.d("로그","LifeStyle on Resume")

        val currentRegionCode = sharedPreferences.getString("current_selected_country", "KR")
        if (currentRegionCode != previousRegionCode) {
            initSpinner(binding, sharedPreferences)
            fetchBrowseData(channelViewModel, "/m/019_rr", currentRegionCode)
            previousRegionCode = currentRegionCode
        }
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        Log.d("로그","LifeStyle on Pause")


    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?){
        if(key == "current_selected_country"){
            initSpinner(binding,sharedPreferences)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBrowseView() = with(binding) {

        previousRegionCode = loadLastRegion(sharedPreferences)

        val navView: View = requireActivity().findViewById(R.id.nav_view)
        val homeBtn: View = requireActivity().findViewById(R.id.iv_home_btn)
        navView.visibility = View.GONE
        homeBtn.visibility = View.GONE

        mlLifeStyleFragment.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Log.d("MotionLayout", "Life: {${requireActivity().resources.getResourceEntryName(currentId)}}")
                bottomNavControl(currentId,navView,homeBtn)

            }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })


        listLayout.tvTopbarTitle.text = getString(browse_life_style)
        listLayout.tvChannelTitle.text = getString(browse_life_style)
        listLayout.tvVideoTitle.text = getString(browse_life_style)

        initSpinner(binding,sharedPreferences)

        listLayout.spinnerSelectRegion.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            val regionCode = when (text) {
                "한국" -> "KR"
                "미국" -> "US"
                "영국" -> "GB"
                "일본" -> "JP"
                else -> "KR"
            }
            if(regionCode != previousRegionCode){
                saveSelectedRegion(sharedPreferences,regionCode)
                fetchBrowseData(channelViewModel,"/m/019_rr",regionCode)
                previousRegionCode = regionCode
            }
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
        previousRegionCode = lastRegionCode

    }



}