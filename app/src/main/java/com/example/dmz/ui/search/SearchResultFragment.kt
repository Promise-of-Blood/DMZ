package com.example.dmz.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
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

        searchViewModel.videoList.observe(viewLifecycleOwner) { searchVideoList ->

            searchResultAdapter.submitList(searchVideoList)

            Log.d("data", searchVideoList.toString())
        }

        searchResultAdapter.setItemClickListener(object : SearchResultAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val videoItem = searchViewModel.videoList.value?.get(position)
                val videoId = videoItem?.videoId ?: "null"

                val action =
                    SearchResultFragmentDirections.actionNavigationSearchResultToNavigationDetail(
                        videoId
                    )

                v.findNavController().navigate(action)

            }

        })

        // searchListViewAdapter.setItemClickListener(object :
        //            SearchListViewAdapter.OnItemClickListener {
        //            override fun onClick(v: View, position: Int) {
        //
        //                val searchItem = searchViewModel.returnSearchItem(position)
        //                if (searchItem != null) {
        //                    if (searchItem.isLiked){
        //                        searchViewModel.updateIsLike(position)
        //                        galleryViewModel.removeGallery(searchItem.uuid)
        //                    }else{
        //                        searchItem?.let { galleryViewModel.addGalleryList(it) }
        //                        searchViewModel.updateIsLike(position)
        //                    }
        //                }
        //            }
        //        })

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        binding.rvSearchResultList.adapter = searchResultAdapter
        binding.rvSearchResultList.layoutManager = LinearLayoutManager(mContext)
    }


}