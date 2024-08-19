package com.example.dmz.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dmz.R
import com.example.dmz.databinding.FragmentSearchBinding
import com.example.dmz.model.SearchEntity
import com.example.dmz.model.listOfSearch
import com.example.dmz.utils.Util
import com.example.dmz.utils.Util.getNowTimeAsIso
import com.example.dmz.utils.Util.setDateAgo
import com.example.dmz.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context

    private val searchViewModel: SearchViewModel by activityViewModels {
        SearchViewModel.SearchViewModelFactory()
    }

    // 검색 변수
    private var searchRegion: String? = null
    private var searchSort: String? = null
    private var searchBeforeDate: String? = null
    private var searchNowDate: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setViewPager()
//        searchViewModel.getRecentSearchItems(mContext)

        searchViewModel.loadRecentSearchItems(mContext)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            spinnerRegion.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                val regionArrayResource = resources.getStringArray(R.array.region_array)
                setRegion(text, regionArrayResource)
            }
            spinnerSort.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                val regionArrayResource = resources.getStringArray(R.array.sort_array)
                setSort(text, regionArrayResource)
            }

            spinnerDate.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                val regionArrayResource = resources.getStringArray(R.array.date_array)
                searchBeforeDate = setDate(text, regionArrayResource)
                Log.d("setDate", searchBeforeDate!!)
            }

            btnSearch.setOnClickListener {
                val query = etSearch.text.toString().trim()

                if (query.isEmpty()) {
                    Toast.makeText(mContext, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (searchRegion == null || searchSort == null || searchNowDate == null) {
                    Toast.makeText(mContext, "설정하지 않은 속성이 있습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val searchItem =
                    SearchEntity(
                        query = query,
                        region = searchRegion!!,
                        sort = searchSort!!,
                        date = searchNowDate!!,
                        color = R.color.flou_yellow
                    )

                doVideoSearch(query)
//                    saveSearchItem(query)
//                    Util.addPrefItem(mContext, searchItem)


                searchViewModel.addRecentSearch(searchItem)
                searchViewModel.saveRecentSearchList(mContext)
                findNavController().navigate(R.id.action_navigation_search_to_navigation_search_result)

            }
        }

        searchViewModel.recentSearchedList.observe(viewLifecycleOwner) { searchedItem ->
            binding.vpRecentSearch.apply {
                adapter = SearchRecentAdapter(searchedItem)
                offscreenPageLimit = 4
                setPageTransformer(SliderTransformer(4))
            }
        }


    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun doVideoSearch(query: String) {
        searchViewModel.doVideoSearch(
            q = query,
            order = searchSort,
            publishedAfter = searchBeforeDate,
            publishedBefore = searchNowDate,
            regionCode = searchRegion
        )
    }

    private fun setViewPager() {
        binding.vpRecentSearch.apply {
            adapter = SearchRecentAdapter(listOfSearch())
            offscreenPageLimit = 4
            setPageTransformer(SliderTransformer(4))
        }
    }

    private fun setRegion(input: String, regionArrayResource: Array<String>) {
        when (input) {
            regionArrayResource[0] -> searchRegion = "KR"
            regionArrayResource[1] -> searchRegion = "US"
            regionArrayResource[2] -> searchRegion = "GB"
            regionArrayResource[3] -> searchRegion = "JP"
        }
        searchRegion?.let { Log.d("setRegion", it) }
    }

    private fun setSort(input: String, regionArrayResource: Array<String>) {
        when (input) {
            regionArrayResource[0] -> searchSort = "relevance"
            regionArrayResource[1] -> searchSort = "date"
            regionArrayResource[2] -> searchSort = "rating"
            regionArrayResource[3] -> searchSort = "title"
            regionArrayResource[4] -> searchSort = "viewCount"
        }
        searchSort?.let { Log.d("setSort", it) }
    }

    private fun setDate(input: String, regionArrayResource: Array<String>): String? {

        searchNowDate = getNowTimeAsIso()

        return searchNowDate?.let {
            when (input) {
                regionArrayResource[0] -> setDateAgo(it, Util.DateType.DATE, 1)

                regionArrayResource[1] -> setDateAgo(it, Util.DateType.DATE, 7)

                regionArrayResource[2] -> setDateAgo(it, Util.DateType.MONTH, 1)

                regionArrayResource[3] -> setDateAgo(it, Util.DateType.YEAR, 1)

                else -> throw IllegalStateException("Invalid RegionArrayResource")
            }
        }

    }


}