package com.example.dmz.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dmz.R
import com.example.dmz.databinding.FragmentBrowseBinding
import com.example.dmz.databinding.FragmentGameBinding


class BrowseFragment : Fragment() {

    private var _binding: FragmentBrowseBinding? = null
    private val binding get() = _binding!!

    private val browseViewPager : BrowseViewPager by lazy { BrowseViewPager(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView()= with(binding){
        vpBrowse.adapter = browseViewPager
        vpBrowse.isUserInputEnabled = false

        previousBtn.setOnClickListener{
            val currentItem = vpBrowse.currentItem
            vpBrowse.currentItem = currentItem - 1
        }

        nextBtn.setOnClickListener{
            val currentItem = vpBrowse.currentItem
            vpBrowse.currentItem = currentItem + 1
        }

    }


}