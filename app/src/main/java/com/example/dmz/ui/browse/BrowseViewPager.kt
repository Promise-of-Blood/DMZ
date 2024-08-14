package com.example.dmz.ui.browse

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BrowseViewPager(fragment : Fragment) : FragmentStateAdapter(fragment){
    val fragments : List<Fragment> = listOf()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}