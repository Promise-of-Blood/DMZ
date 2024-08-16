package com.example.dmz.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.dmz.R
import com.example.dmz.databinding.FragmentSearchBinding
import com.example.dmz.model.listOfSearch
import com.example.dmz.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.SearchViewModelFactory()
    }

    // 검색 변수
    private lateinit var searchRegion: String
    private lateinit var searchSort: String

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initSpinner(requireContext(), binding.spRegion)
        binding.spinnerRegion.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            val regionArrayResource = resources.getStringArray(R.array.region_array)
            setRegion(text, regionArrayResource)
        }
        binding.spinnerSort.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            val regionArrayResource = resources.getStringArray(R.array.sort_array)
            setSort(text, regionArrayResource)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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
        Log.d("setRegion", searchRegion)
    }

    private fun setSort(input: String, regionArrayResource: Array<String>) {
        when (input) {
            regionArrayResource[0] -> searchSort = "relevance"
            regionArrayResource[1] -> searchSort = "date"
            regionArrayResource[2] -> searchSort = "rating"
            regionArrayResource[3] -> searchSort = "title"
            regionArrayResource[4] -> searchSort = "viewCount"
        }
        Log.d("setSort", searchSort)
    }




}