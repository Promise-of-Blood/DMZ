package com.example.dmz.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.dmz.R
import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.model.Keywords
import com.example.dmz.data.repository.KeywordsRepositoryImpl
import com.example.dmz.databinding.FragmentHomeBinding
import com.example.dmz.viewmodel.HomeViewModel
import com.example.dmz.viewmodel.HomeViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.abs

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var isSyncingScroll = false

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        homeViewModel.getKeywordsList()
        Log.i("process test", "initiViewModel 실행중")
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.keywordsList.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .filter { it.isNotEmpty() }
                .collectLatest { keywordsList ->
                    Log.i("Keywords List", keywordsList.toString())

                    setupViewPager(keywordsList)
                    setupCalendar(keywordsList)
                    setupChart(binding.lineChart, keywordsList)
                    setupScrollSync()

                }
        }
    }

    private fun setupScrollSync() {
        binding.apply {
            rvCalendar.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!isSyncingScroll) {
                        isSyncingScroll = true
                        binding.hsvChart.scrollBy(dx, 0)
                        isSyncingScroll = false
                    }
                }
            })

            hsvChart.setOnScrollChangeListener { _, scrollX, _, _, _ ->
                if (!isSyncingScroll) {
                    isSyncingScroll = true
                    binding.rvCalendar.scrollBy(
                        scrollX - binding.rvCalendar.computeHorizontalScrollOffset(),
                        0
                    )
                    isSyncingScroll = false
                }
            }
        }
    }

    private fun setupCalendar(keywordsList: List<Keywords>) {
        Log.i("process test", "setupCalendar 실행중")
        val dateSet = mutableListOf<Date>()
        val currentDate = LocalDate.now()

        for (i in 0..13) {
            val dateToAdd = currentDate.minusDays(i.toLong())

            // 요일을 계산
            val dayOfWeek = when (dateToAdd.dayOfWeek) {
                DayOfWeek.MONDAY -> "Mon"
                DayOfWeek.TUESDAY -> "Tue"
                DayOfWeek.WEDNESDAY -> "Wed"
                DayOfWeek.THURSDAY -> "Thu"
                DayOfWeek.FRIDAY -> "Fri"
                DayOfWeek.SATURDAY -> "Sat"
                DayOfWeek.SUNDAY -> "Sun"
            }

            val dayOfMonth = dateToAdd.dayOfMonth

            // Date 객체를 생성하여 리스트에 추가
            val date = Date(dayOfWeek, dayOfMonth.toString())
            dateSet.add(date)
        }
        //TODO Keywords 객체 하나만 전달 하기
        val adapter = DayAdapter(dateSet.reversed(), keywordsList[0])
        binding.apply {
            rvCalendar.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvCalendar.adapter = adapter
            rvCalendar.overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    private fun setupChart(chart: LineChart, keywordsList: List<Keywords>) {
        Log.i("process test", "setupChart 실행중")
        if (keywordsList.isNotEmpty()) {
            // 화면 너비를 가져오기 위해 DisplayMetrics를 사용
            val displayMetrics = chart.context.resources.displayMetrics
            val deviceWidth = displayMetrics.widthPixels

            // LineChart의 레이아웃 파라미터를 수정하여 가로 길이를 디바이스 너비의 두 배로 설정
            val layoutParams = chart.layoutParams
            layoutParams.width = deviceWidth * 2
            chart.layoutParams = layoutParams

            chart.apply {
                setPinchZoom(false)
                setScaleEnabled(false)
                setTouchEnabled(false)
                isDoubleTapToZoomEnabled = false
                xAxis.isEnabled = false
                xAxis.setLabelCount(4, true)
                axisRight.isEnabled = false
                axisLeft.isEnabled = false
                legend.isEnabled = false
                description.isEnabled = false
                animateY(500)
            }


            chart.apply {
                val entryList = arrayListOf<Entry>()
                val recentTrend = keywordsList[0].recentTrend
                recentTrend.forEachIndexed { index, d ->
                    entryList.add(Entry(index.toFloat(), d.toFloat()))
                }

                val lineDataSet = LineDataSet(entryList, "data").apply {
                    setDrawCircles(false)
                    lineWidth = 2.0F
                    setDrawValues(false)
                    color = ContextCompat.getColor(chart.context, R.color.flou_yellow)
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                }
                data = LineData(listOf(lineDataSet))
                invalidate()
            }
        }

    }


    private fun setupViewPager(keywordsList: List<Keywords>) {
        Log.i("process test", "setupViewPager 실행중")

        val adapter = KeywordAdapter(keywordsList)

        binding.apply {
            vpTodayKeyword.adapter = adapter
            (vpTodayKeyword.getChildAt(0) as? RecyclerView)?.also {
                it.overScrollMode = View.OVER_SCROLL_NEVER
            }

            val compositePageTransformer = CompositePageTransformer().apply {

                addTransformer(MarginPageTransformer(50))

                addTransformer { page, position ->
                    val scaleFactor = if (position in -1f..1f) {
                        0.85f + (1 - abs(position)) * 0.15f
                    } else {
                        0.85f
                    }
                    val imageView = page.findViewById<ImageView>(R.id.iv_keyword_image)
                    imageView?.apply {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }

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