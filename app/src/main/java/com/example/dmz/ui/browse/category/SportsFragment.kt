package com.example.dmz.ui.browse.category

import ItemMarginDecoration
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Path
import com.example.dmz.utils.Util.wiggle
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dmz.R
import com.example.dmz.R.string.browse_life_style
import com.example.dmz.R.string.browse_sports
import com.example.dmz.data.repository.SearchRepositoryImpl
import com.example.dmz.databinding.FragmentSportsBinding
import com.example.dmz.remote.YoutubeSearchClient
import com.example.dmz.ui.browse.ChannelListAdapter
import com.example.dmz.ui.browse.VideoListAdapter
import com.example.dmz.ui.browse.bottomNavControl
import com.example.dmz.ui.browse.fetchBrowseData
import com.example.dmz.ui.browse.initSpinner
import com.example.dmz.ui.browse.loadLastRegion
import com.example.dmz.ui.browse.saveSelectedRegion
import com.example.dmz.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import java.io.ObjectStreamException
import kotlin.random.Random


class SportsFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var sharedPreferences: SharedPreferences
    private var previousRegionCode : String? = null

    private var _binding : FragmentSportsBinding? = null
    private val binding get() = _binding!!

    private val browseChannelAdapter by lazy { ChannelListAdapter() }
    private val browseVideoAdapter by lazy { VideoListAdapter() }

    private val channelViewModel: SearchViewModel by viewModels {
        viewModelFactory { initializer { SearchViewModel(SearchRepositoryImpl(YoutubeSearchClient.youtubeApi)) } }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSportsBinding.inflate(inflater,container,false)
        sharedPreferences = requireContext().getSharedPreferences("regionCode", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBrowseView()
        initBrowseViewModel()

        val motionLayout = binding.introLayout.mlSportsIntro

        val sportsLetters = listOf(
            binding.introLayout.sportsLetterS,
            binding.introLayout.sportsLetterP,
            binding.introLayout.sportsLetterO,
            binding.introLayout.sportsLetterR,
            binding.introLayout.sportsLetterT,
            binding.introLayout.sportsLetterS2)

        val tennisBall = binding.introLayout.sportsTennisBall
        val bascketBall = binding.introLayout.sportsBascketBall
        val bubble1 = binding.introLayout.sportsBubble1
        val bubble2 = binding.introLayout.sportsBubble2
        val bubble3 = binding.introLayout.sportsBubble3
        val water = binding.introLayout.sportsWaterSplash

        wiggle(water,1000,0)


        bubbleAnimation(bubble1)
        bubbleAnimation(bubble2)
        bubbleAnimation(bubble3)

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(currentId == R.id.end){
                    sportsLetters.forEachIndexed { index, view ->
                        val duration = Random.nextLong(1000, 3000)
                        val delay = index * 100L
                        wiggle(view,duration,delay)
                    }

                    tennisAnimation(tennisBall)
                    bascketBallAnimation(bascketBall)
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
//            val regionCode = sharedPreferences.getString(key, "KR")
//            fetchBrowseData(channelViewModel,"/m/0bzvm2", regionCode)
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

        mlSportsFragment.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Log.d("MotionLayout", "Game: {${requireActivity().resources.getResourceEntryName(currentId)}}")
                bottomNavControl(currentId,navView,homeBtn)

            }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })


        listLayout.tvTopbarTitle.text = getString(browse_sports)
        listLayout.tvChannelTitle.text = getString(browse_sports)
        listLayout.tvVideoTitle.text = getString(R.string.browse_sports)


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
              fetchBrowseData(channelViewModel,"/m/06ntj",regionCode)
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

        val lastRegionCode = loadLastRegion(sharedPreferences)
        fetchBrowseData(channelViewModel,"/m/06ntj",lastRegionCode)


        channelViewModel.channelList.observe(viewLifecycleOwner) { channels ->
            browseChannelAdapter.submitList(channels)
        }

        channelViewModel.videoList.observe(viewLifecycleOwner) { videos ->
            browseVideoAdapter.submitList(videos)
            previousRegionCode = lastRegionCode
        }


    }



    private fun tennisAnimation(view: View){
        val path1 = Path().apply{
            moveTo(view.x,view.y)
            quadTo(150f,-300f,1200f,-100f)
        }
        val path2 = Path().apply {
            moveTo(1200f,0f)
            quadTo(150f,0f,-300f,100f)
        }

        val animatorLtoR = ObjectAnimator.ofFloat(view,View.X,View.Y,path1).apply {
            val startDelayValue = Random.nextLong(0,2000)
            startDelay = startDelayValue
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val animatorRtoL = ObjectAnimator.ofFloat(view, View.X, View.Y, path2).apply {
            val startDelayValue = Random.nextLong(1000,3000)
            startDelay = 1000
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val animationSetting = AnimatorSet().apply {
            playSequentially(animatorLtoR, animatorRtoL)
            addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    start()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })


        }
        animationSetting.start()
    }

    private fun bascketBallAnimation(view: View) {

        val firstScaleX = ObjectAnimator.ofFloat(view,"scaleX",0.8f,1.2f).apply {
            interpolator = AccelerateDecelerateInterpolator()
        }
        val firstScaleY = ObjectAnimator.ofFloat(view,"scaleY",0.8f,1.2f).apply {
            interpolator = AccelerateDecelerateInterpolator()
        }
        val firstTranslateX = ObjectAnimator.ofFloat(view, "translationX", -500f, 1200f)
        val firstTranslateY = ObjectAnimator.ofFloat(view, "translationY", 600f, 1800f).apply {
            interpolator = BounceInterpolator()
        }

        val secondTranslateX = ObjectAnimator.ofFloat(view, "translationX", 1200f, -100f)
        val secondRotate = ObjectAnimator.ofFloat(view, "rotation", 0f, -360f).apply {
            interpolator = AccelerateDecelerateInterpolator()
        }


        val firstAnimationSetting = AnimatorSet().apply {
            playTogether(firstTranslateX, firstTranslateY,firstScaleX,firstScaleY)
            duration = 2000
        }

        val secondAnimationSetting = AnimatorSet().apply {
            playTogether(secondTranslateX, secondRotate)
            duration = 3000
        }

        val fullAnimation = AnimatorSet().apply {
            playSequentially(firstAnimationSetting, secondAnimationSetting)
        }

        fullAnimation.start()


    }

    private fun bubbleAnimation(bubble: View) {
        // 화면 너비에 따라 랜덤한 X 좌표 설정
        val context = context ?: return

        val screenWidth = resources.displayMetrics.widthPixels
        val startX = Random.nextInt(0, screenWidth).toFloat()

        val randomDuration = Random.nextLong(2000,5000)
        val randomStartDelay = Random.nextLong(0, 2000)

        // 비눗방울의 초기 위치를 랜덤하게 설정
        bubble.translationX = startX
        bubble.translationY = 2000f // 아래에서 시작
        bubble.alpha = 0f

        // 위로 올라가는 애니메이션
        val translateY = ObjectAnimator.ofFloat(bubble, "translationY", 2000f, -400f).apply {

            startDelay = randomStartDelay
            duration = randomDuration
            interpolator = DecelerateInterpolator() // 처음에 빠르고, 점점 느려짐
        }



        // 크기 변화 애니메이션
        val scaleX = ObjectAnimator.ofFloat(bubble, "scaleX", 1f, 0.6f).apply {
            duration = randomDuration
            startDelay = randomStartDelay
        }
        val scaleY = ObjectAnimator.ofFloat(bubble, "scaleY", 1f,0.6f).apply {
            duration = randomDuration
            startDelay = randomStartDelay
        }

        // 투명도 변화 애니메이션
        val alpha = ObjectAnimator.ofFloat(bubble, "alpha", 0f, 1f).apply {
            duration = randomDuration /2
            startDelay = randomStartDelay
        }

        // 모든 애니메이션을 동시에 실행
        val animatorSet = AnimatorSet().apply {
            playTogether(translateY, scaleX, scaleY, alpha)
            addListener(object : android.animation.Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    if(context != null && isAdded){
                        bubbleAnimation(bubble)
                    }

                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
        }

        // 애니메이션 시작
        animatorSet.start()
    }

    
}