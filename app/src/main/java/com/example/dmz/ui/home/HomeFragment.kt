package com.example.dmz.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.dmz.R
import com.example.dmz.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.abs

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
        setupChart()
        return binding.root
    }


    private fun setupChart() {
        binding.lineChart.apply {
            // zoom disabled.
            setPinchZoom(false)
            setScaleEnabled(true)
            setTouchEnabled(false)

            isDoubleTapToZoomEnabled = true

            // right, left, x axis disabled.
            // legend, description disabled.
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            xAxis.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false
            animateY(500)
        }

        val dataList = arrayListOf(
            1, 5, 3, 4, 3, 1, 2
        )

        binding.lineChart.apply {
            val entryList = arrayListOf<Entry>()
            dataList.forEachIndexed { index, d ->
                entryList.add(Entry(index.toFloat(), d.toFloat()))
            }
            val lineDataSet = LineDataSet(entryList, "data").apply {

                // circle 안나오게 하는 코드
                setDrawCircles(false)
                // 선 두께
                lineWidth = 2.0F

                // 지점별 값 나타내기
                setDrawValues(true)

                color = ContextCompat.getColor(requireContext(), R.color.flou_yellow)

                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            data = LineData(listOf(lineDataSet))

            invalidate()
        }
    }


    private fun setupViewPager() {
        val images = listOf(
            Keyword(R.drawable.img_keyword_yoajung, "요아정1"),
            Keyword(R.drawable.img_keyword_yoajung, "요아정2"),
            Keyword(R.drawable.img_keyword_yoajung, "요아정3"),
            Keyword(R.drawable.img_keyword_yoajung, "요아정4"),
            Keyword(R.drawable.img_keyword_yoajung, "요아정5"),
            Keyword(R.drawable.img_keyword_yoajung, "요아정6")
        )
        val adapter = KeywordAdapter(images)

        binding.run {
            vpTodayKeyword.adapter = adapter

            // Set up CompositePageTransformer
            val compositePageTransformer = CompositePageTransformer().apply {

                addTransformer(MarginPageTransformer(50))

                addTransformer { page, position ->
                    val scaleFactor = if (position in -1f..1f) {
                        0.85f + (1 - abs(position)) * 0.15f
                    } else {
                        0.85f
                    }
                    // Adjust the ImageView scale
                    val imageView = page.findViewById<ImageView>(R.id.iv_keyword_image)
                    imageView?.apply {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    // Optionally adjust alpha
                    page.alpha = 0.5f + (1 - abs(position))
                }
            }

            vpTodayKeyword.apply {
                setPageTransformer(compositePageTransformer)
                offscreenPageLimit = 2
                setPadding(200, 0, 200, 0)
                clipToPadding = false // Ensure padding is not clipped
            }

            ivArrowLeft.setOnClickListener {
                vpTodayKeyword.setCurrentItem(vpTodayKeyword.currentItem - 1, true)
            }

            ivArrowRight.setOnClickListener {
                vpTodayKeyword.setCurrentItem(vpTodayKeyword.currentItem + 1, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}