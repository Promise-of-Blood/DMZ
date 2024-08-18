package com.example.dmz.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dmz.databinding.FragmentSearchResultBinding
import com.example.dmz.viewmodel.SearchViewModel

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context

    private val searchViewModel: SearchViewModel by activityViewModels {
        SearchViewModel.SearchViewModelFactory()
    }

    private val searchListAdapter by lazy { SearchResultAdapter() }

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
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        binding.rvSearchResultList.adapter = searchListAdapter
        binding.rvSearchResultList.layoutManager = LinearLayoutManager(mContext)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.videoList.observe(viewLifecycleOwner) { searchVideoList ->

            searchListAdapter.submitList(searchVideoList)

            Log.d("data", searchVideoList.toString())
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }



}