package com.example.dmz.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dmz.R
import com.example.dmz.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewPager()
        Log.d("HomeFragment", "onCreateView")
        return binding.root
    }


    private fun setupViewPager() {
        val images = listOf(R.drawable.img_keyword_yoajung, R.drawable.img_keyword_yoajung, R.drawable.img_keyword_yoajung)
        val keywords = listOf("요아정1", "요아정2", "요아정3")
        val imageAdapter = ImagePagerAdapter(images, requireContext())

        binding.run {
            rvTodayKeywordImage.adapter = imageAdapter
            rvTodayKeywordImage.offscreenPageLimit = 3
            rvTodayKeywordImage.setPageTransformer { page, position ->
                val scaleFactor = 0.85f.coerceAtLeast(1 - kotlin.math.abs(position))
                page.scaleY = scaleFactor
                page.alpha = scaleFactor
                page.translationX = 100 * position
            }

            rvTodayKeywordImage.apply {
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 1
                setPadding(200, 0, 200, 0) // 좌우 패딩 추가
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}