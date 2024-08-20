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
import com.example.dmz.MainActivity
import com.example.dmz.R
import com.example.dmz.databinding.FragmentSearchBinding
import com.example.dmz.model.SearchEntity
import com.example.dmz.utils.Util
import com.example.dmz.utils.Util.getNowTimeAsIso
import com.example.dmz.utils.Util.handleBottomNavigationVisibility
import com.example.dmz.utils.Util.koreanToRegionCode
import com.example.dmz.utils.Util.koreanToSortData
import com.example.dmz.utils.Util.setDateAgo
import com.example.dmz.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context

    private val searchViewModel: SearchViewModel by activityViewModels {
        SearchViewModel.SearchViewModelFactory()
    }

    private lateinit var searchRecentAdapter: SearchRecentAdapter

    // 검색 변수
    private var searchRegion: String? = null
    private var searchSort: String? = null
    private var searchBeforeDate: String? = null
    private var searchNowDate: String? = null
    private var searchDateSet: String? = null
    private var maxResults: Int = 5
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

        searchViewModel.loadRecentSearchItems(mContext)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).handleBottomNavigationVisibility(false)

        setSpinners()
        setSearchButton()
        setRecentSearchList()

    }

    override fun onDestroy() {
        _binding = null
        (activity as MainActivity).handleBottomNavigationVisibility(true)
        super.onDestroy()
    }

    private fun setRecentSearchList() {
        searchViewModel.recentSearchedList.observe(viewLifecycleOwner) { searchedItem ->
            binding.vpRecentSearch.apply {
                searchRecentAdapter = SearchRecentAdapter(searchedItem)
                adapter = searchRecentAdapter
                offscreenPageLimit = 4
                setPageTransformer(SliderTransformer(4))
            }

            setRecentClick()

        }
    }

    private fun setRecentClick() {
        searchRecentAdapter.setItemClickListener(object :
            SearchRecentAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val searchItem = searchViewModel.recentSearchedList.value?.get(position)
                if (searchItem == null) {
                    Log.d("SearchItemError", "searchItem is null")
                }
                searchItem?.let {
                    binding.run {
                        etSearch.setText(it.query)
                        val region = koreanToRegionCode(it.region)
                        searchRegion = it.region
                        spinnerRegion.setText(region)

                        val sort = koreanToSortData(it.sort)
                        searchSort = it.sort
                        spinnerSort.setText(sort)

                        searchDateSet = it.date
                        spinnerDate.setText(it.date)
                    }
                }
            }

        })
    }

    private fun setSearchButton() {
        binding.run {
            btnSearch.setOnClickListener {
                val query = etSearch.text.toString().trim()

                if (query.isEmpty()) {
                    Toast.makeText(mContext, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (searchRegion == null || searchSort == null || searchDateSet == null) {
                    Toast.makeText(mContext, "설정하지 않은 속성이 있습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val searchItem =
                    SearchEntity(
                        query = query,
                        region = searchRegion!!,
                        sort = searchSort!!,
                        date = searchDateSet!!,
                        color = R.color.flou_yellow
                    )

                maxResults = 50
                doVideoSearch(query = query, max = maxResults)

                searchViewModel.addRecentSearch(searchItem)
                searchViewModel.saveRecentSearchList(mContext)

                val action =
                    SearchFragmentDirections.actionNavigationSearchToNavigationSearchResult(
                        searchItem.query, searchItem.region, searchItem.sort, searchItem.date
                    )

                findNavController().navigate(action)

            }
        }
    }

    private fun setSpinners() {
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
                searchDateSet = text
                searchBeforeDate = setDate(text, regionArrayResource)
                Log.d("setDate", searchBeforeDate!!)
            }
        }
    }

    private fun doVideoSearch(query: String, max: Int) {
        searchViewModel.doVideoSearch(
            q = query,
            order = searchSort,
            publishedAfter = searchBeforeDate,
            publishedBefore = searchNowDate,
            regionCode = searchRegion,
            maxResults = max
        )
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