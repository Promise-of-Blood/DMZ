package com.example.dmz.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dmz.MainActivity
import com.example.dmz.databinding.FragmentSearchResultBinding
import com.example.dmz.utils.Util.handleBottomNavigationVisibility
import com.example.dmz.utils.Util.koreanToRegionCode
import com.example.dmz.utils.Util.koreanToSortData
import com.example.dmz.viewmodel.SearchViewModel

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context

    private val args: SearchResultFragmentArgs by navArgs()


    private val searchViewModel: SearchViewModel by activityViewModels {
        SearchViewModel.SearchViewModelFactory()
    }

    private val searchResultAdapter by lazy { SearchResultAdapter() }

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
        initView(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeVideoData()
        initItemClick()
    }

    override fun onResume() {
        (activity as MainActivity).handleBottomNavigationVisibility(false)
        super.onResume()
    }

    override fun onDestroy() {
        _binding = null
        (activity as MainActivity).handleBottomNavigationVisibility(true)
        super.onDestroy()
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        binding.run {
            rvSearchResultList.adapter = searchResultAdapter
            rvSearchResultList.layoutManager = LinearLayoutManager(mContext)

            spinnerResult.setOnSpinnerItemSelectedListener<String> { _, _, _, _ ->
                spinnerResult.setText(args.query)
            }

            val region = koreanToRegionCode(args.region)
            val sort = koreanToSortData(args.sort)
            val items = listOf(region, sort, args.date)
            spinnerResult.setText(args.query)
            spinnerResult.setItems(items)
        }
    }

    private fun observeVideoData() {
        searchViewModel.videoList.observe(viewLifecycleOwner) { searchVideoList ->

            searchResultAdapter.submitList(searchVideoList)

            Log.d("data", searchVideoList.toString())
        }
    }

    private fun initItemClick() {
        searchResultAdapter.setItemClickListener(object : SearchResultAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val videoItem = searchViewModel.videoList.value?.get(position)
                val videoId = videoItem?.videoId ?: "null"
                val thumbnail = videoItem?.thumbnail ?: "null"

                val action =
                    SearchResultFragmentDirections.actionNavigationSearchResultToNavigationDetail(
                        videoId,
                        thumbnail
                    )

                v.findNavController().navigate(action)

            }
        })
    }


}