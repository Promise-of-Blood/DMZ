package com.example.dmz.ui.browse

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dmz.ui.browse.category.GameFragment
import com.example.dmz.ui.browse.category.LifeStyleFragment
import com.example.dmz.ui.browse.category.MovieFragment
import com.example.dmz.ui.browse.category.MusicFragment
import com.example.dmz.ui.browse.category.SportsFragment

class BrowseViewPager(fragment : Fragment) : FragmentStateAdapter(fragment){
    val fragments : List<Fragment> = listOf(
        GameFragment(),
        LifeStyleFragment(),
        MusicFragment(),
        SportsFragment(),
        MovieFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}