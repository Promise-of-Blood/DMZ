package com.example.dmz.ui.browse.category

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
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
import com.example.dmz.R
import com.example.dmz.databinding.FragmentMovieBinding
import com.example.dmz.utils.Util.wiggle
import kotlinx.coroutines.Job
import kotlin.random.Random
import kotlin.time.Duration


class MovieFragment : Fragment() {

    private var _binding : FragmentMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startPopcornAnimation()
        rotateAnimation(binding.movieLayout.movieCamera,15f,30f,3000)
        rotateAnimation(binding.movieLayout.movieFilm,15f,30f,2000)
        rotateAnimation(binding.movieLayout.moviePopcornCase,0f,15f,250)
        rotateAnimation(binding.movieLayout.movieCola,240f,200f,2500)


        val letters = listOf(
            binding.movieLayout.movieLetterM,
            binding.movieLayout.movieLetterO,
            binding.movieLayout.movieLetterV,
            binding.movieLayout.movieLetterI,
            binding.movieLayout.movieLetterE
        )

        val motionLayout = binding.movieLayout.mlMovieIntro

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
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
        val popcornBox = binding.movieLayout.moviePopcornCase // 팝콘 상자 뷰

        parentLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                parentLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)


                for (i in 1..15) { // 튀어오르는 팝콘 개수
                    // 팝콘 뷰 생성
                    val popcorn = ImageView(requireContext()).apply {
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


