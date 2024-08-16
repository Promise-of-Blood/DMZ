package com.example.dmz.ui.browse.category

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dmz.R
import com.example.dmz.databinding.FragmentLifeStyleBinding
import kotlin.random.Random


class LifeStyleFragment : Fragment() {
    private var _binding : FragmentLifeStyleBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLifeStyleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val letters = listOf(
            binding.lifestyleLayout.lifestyleLetterL,
            binding.lifestyleLayout.lifestyleLetterI,
            binding.lifestyleLayout.lifestyleLetterF,
            binding.lifestyleLayout.lifestyleLetterE,
            binding.lifestyleLayout.lifestyleLetterS,
            binding.lifestyleLayout.lifestyleLetterT,
            binding.lifestyleLayout.lifestyleLetterY,
            binding.lifestyleLayout.lifestyleLetterL2,
            binding.lifestyleLayout.lifestyleLetterE2
        )

        letters.forEachIndexed{index, view ->
            val duration = Random.nextLong(1000, 3000)
            val delay = Random.nextLong(0,500)
            wiggle(view,duration,delay)
        }

        wiggle(binding.lifestyleLayout.lifestyle3dFlower,2000,0)

    }

    private fun wiggle(view:View, duration:Long, delay: Long){
        val firstValueY = Random.nextFloat()*30-30
        val secondValueY = Random.nextFloat()*20-0
        val animator = ObjectAnimator.ofFloat(view, "translationY", firstValueY, secondValueY)
        animator.duration = duration
        animator.startDelay = delay
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }
}