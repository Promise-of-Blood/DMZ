package com.example.dmz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.dmz.R
import com.example.dmz.databinding.FragmentSearchBinding
import com.example.dmz.model.listOfSearch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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

    private fun setViewPager() {
        binding.vpRecentSearch.apply {
            adapter = SearchRecentAdapter(listOfSearch())
            offscreenPageLimit = 4
            setPageTransformer(SliderTransformer(4))
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}