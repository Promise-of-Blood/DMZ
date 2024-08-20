package com.example.dmz.ui.browse.category

import ItemMarginDecoration
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dmz.R
import com.example.dmz.R.string.browse_life_style
import com.example.dmz.R.string.browse_movie
import com.example.dmz.data.repository.SearchRepositoryImpl
import com.example.dmz.databinding.FragmentMovieBinding
import com.example.dmz.remote.YoutubeSearchClient
import com.example.dmz.ui.browse.ChannelListAdapter
import com.example.dmz.ui.browse.VideoListAdapter
import com.example.dmz.ui.browse.bottomNavControl
import com.example.dmz.ui.browse.fetchBrowseData
import com.example.dmz.ui.browse.initSpinner
import com.example.dmz.ui.browse.loadLastRegion
import com.example.dmz.ui.browse.saveSelectedRegion
import com.example.dmz.utils.Util.wiggle
import com.example.dmz.viewmodel.SearchViewModel
import kotlinx.coroutines.Job
import kotlin.random.Random
import kotlin.time.Duration


class MovieFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var sharedPreferences: SharedPreferences
    private var previousRegionCode : String? = null

    private var _binding : FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val browseChannelAdapter by lazy { ChannelListAdapter() }
    private val browseVideoAdapter by lazy { VideoListAdapter() }

    private val channelViewModel: SearchViewModel by viewModels {
        viewModelFactory { initializer { SearchViewModel(SearchRepositoryImpl(YoutubeSearchClient.youtubeApi)) } }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = requireContext().getSharedPreferences("regionCode", Context.MODE_PRIVATE)
        _binding = FragmentMovieBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBrowseView()
        initBrowseViewModel()


        rotateAnimation(binding.introLayout.movieCamera,15f,30f,3000)
        rotateAnimation(binding.introLayout.movieFilm,15f,30f,2000)
        rotateAnimation(binding.introLayout.moviePopcornCase,0f,15f,250)
        rotateAnimation(binding.introLayout.movieCola,240f,200f,2500)

        startPopcornAnimation()

        val letters = listOf(
            binding.introLayout.movieLetterM,
            binding.introLayout.movieLetterO,
            binding.introLayout.movieLetterV,
            binding.introLayout.movieLetterI,
            binding.introLayout.movieLetterE
        )
        val motionLayout = binding.mlMovieFragment

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })

        val introMotionLayout = binding.introLayout.mlMovieIntro

        introMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(currentId == R.id.end){
                    letters.forEachIndexed { index, view ->
                        val duration = Random.nextLong(1000, 3000)
                        val delay = index * 100L
                        wiggle(view,duration,delay)


                    }

                }

            }
        })


    }

    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val currentRegionCode = sharedPreferences.getString("current_selected_country", "KR")
        if (currentRegionCode != previousRegionCode) {
            initSpinner(binding,sharedPreferences)
            fetchBrowseData(channelViewModel, "/m/0bzvm2", currentRegionCode)
            previousRegionCode = currentRegionCode
        }
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?){
        if(key == "current_selected_country"){
            initSpinner(binding,sharedPreferences)
            val regionCode = sharedPreferences.getString(key, "KR")
            fetchBrowseData(channelViewModel,"/m/0bzvm2", regionCode)
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

        mlMovieFragment.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Log.d("MotionLayout", "Game: {${requireActivity().resources.getResourceEntryName(currentId)}}")
                bottomNavControl(currentId,navView,homeBtn)

            }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })

        listLayout.tvTopbarTitle.text = getString(browse_movie)
        listLayout.tvChannelTitle.text = getString(browse_movie)
        listLayout.tvVideoTitle.text = getString(R.string.browse_movie)


        initSpinner(binding,sharedPreferences)
        listLayout.spinnerSelectRegion.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            val regionCode = when (text) {
                "한국" -> "KR"
                "미국" -> "US"
                "영국" -> "GB"
                "일본" -> "JP"
                else -> "KR"
            }
            if(regionCode != previousRegionCode) {
                saveSelectedRegion(sharedPreferences, regionCode)
                fetchBrowseData(channelViewModel,"/m/0bzvm2",regionCode)
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
        fetchBrowseData(channelViewModel,"/m/0bzvm2",lastRegionCode)
        previousRegionCode = lastRegionCode

    }

    private fun rotateAnimation(view: View,firstValue : Float, secondValue :Float,time: Long){

        val rotateAnimator = ObjectAnimator.ofFloat(view,"rotation",firstValue,secondValue).apply {
            duration = time
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        rotateAnimator.start()

    }

    private fun startPopcornAnimation() {
        val parentLayout = binding.root
        val popcornBox = binding.introLayout.moviePopcornCase // 팝콘 상자 뷰

        parentLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                parentLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if(context == null || !isAdded)return

                for (i in 1..15) { // 튀어오르는 팝콘 개수
                    // 팝콘 뷰 생성
                    val popcorn = ImageView(context).apply {
                        val popcornImages = listOf(
                            R.drawable.movie_popcorn_1,
                            R.drawable.movie_popcorn_2,
                            R.drawable.movie_popcorn_3,
                            R.drawable.movie_popcorn_4
                        )
                        val randomResource = popcornImages.random()
                        setImageResource(randomResource) // 팝콘 이미지

                        val randomX = Random.nextInt(150,500).toFloat()
                        val randomY = Random.nextInt(1592,1698).toFloat()

                        layoutParams = ViewGroup.LayoutParams(100, 100)
                        x = randomX
                        y = randomY

                        id = View.generateViewId()

                        parentLayout.addView(this) // 부모 레이아웃에 팝콘 추가
                    }

                    // 각 팝콘에 대해 랜덤 방향과 속도 설정
                    animatePopcorn(popcorn)
                }

            }
        })


    }

    private fun animatePopcorn(popcorn: ImageView) {
        val randomX = Random.nextInt(-200, 850).toFloat() // 좌우로 퍼지는 범위
        val randomY = Random.nextInt(-800, 100).toFloat() // 위로 튀어오르는 범위
        val randomRotation = Random.nextInt(0, 360).toFloat() // 회전 각도
        val randomDuration = Random.nextLong(2000, 3000) // 애니메이션 지속 시간

        // X축으로 퍼지는 애니메이션
        val translateX = ObjectAnimator.ofFloat(popcorn, "translationX", popcorn.translationX, popcorn.translationX + randomX).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            interpolator = DecelerateInterpolator()
        }

        // Y축으로 퍼지는 애니메이션
        val translateY = ObjectAnimator.ofFloat(popcorn, "translationY", popcorn.translationY, popcorn.translationY + randomY).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            interpolator = DecelerateInterpolator()
        }

        // 회전 애니메이션
        val rotate = ObjectAnimator.ofFloat(popcorn, "rotation", 0f, randomRotation).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }

        // 크기 변화 애니메이션 (팝콘이 작아지거나 커지면서 튀어오르도록)

        val randomScale = Random.nextFloat()*(2.0f-0.5f) +0.5f
        val scaleX = ObjectAnimator.ofFloat(popcorn, "scaleX", 1f, randomScale).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }
        val scaleY = ObjectAnimator.ofFloat(popcorn, "scaleY", 1f, randomScale).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART }


//        // 투명도 변화 애니메이션 (팝콘이 사라지도록)
        val key0 = Keyframe.ofFloat(0.8f,1f)
        val key1 = Keyframe.ofFloat(1f,0f)

        val alphaValue = PropertyValuesHolder.ofKeyframe("alpha", key0, key1)
        val alpha = ObjectAnimator.ofPropertyValuesHolder(popcorn, alphaValue).apply {
            duration = 10000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }

        // 애니메이션을 동시에 실행
        val animatorSet = AnimatorSet().apply {
            playTogether(translateX, translateY, rotate, scaleX,scaleY,alpha)
            duration = randomDuration
            addListener(object : android.animation.Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationEnd(animation: android.animation.Animator) {
                    (popcorn.parent as ViewGroup).removeView(popcorn) // 애니메이션 끝나면 팝콘 제거
                }
            })
        }

        // 애니메이션 시작
        animatorSet.start()
    }
}


