package com.example.dmz.ui.browse

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.dmz.R
import com.example.dmz.databinding.FragmentGameBinding
import com.example.dmz.databinding.FragmentLifeStyleBinding
import com.example.dmz.databinding.FragmentMovieBinding
import com.example.dmz.databinding.FragmentMusicBinding
import com.example.dmz.databinding.FragmentSportsBinding
import com.example.dmz.viewmodel.SearchViewModel


fun fetchBrowseData(channelViewModel: SearchViewModel,topic:String, regionCode: String?) {
    channelViewModel.getChannelList(
        topicId = topic,
        maxResults = 5,
        regionCode = regionCode
    )
    channelViewModel.getVideoList(
        topicId = topic,
        maxResults = 10,
        regionCode = regionCode
    )
}

fun saveSelectedRegion(sharedPreferences: SharedPreferences, regionCode: String) {
    val editor = sharedPreferences.edit()
    editor.putString("current_selected_country", regionCode)
    editor.apply()
}

fun loadLastRegion(sharedPreferences: SharedPreferences): String? {
    return sharedPreferences.getString("current_selected_country", "KR")
}

fun initSpinner(binding: ViewBinding, sharedPreferences: SharedPreferences) {
    val lastRegionCode = loadLastRegion(sharedPreferences)
    val regionCode = when (lastRegionCode) {
        "KR" -> 0
        "US" -> 1
        "GB" -> 2
        "JP" -> 3
        else -> 0
    }

    if (lastRegionCode != null) {
        when (binding) {
            is FragmentGameBinding -> {
                binding.listLayout.spinnerSelectRegion.selectItemByIndex(regionCode)
            }

            is FragmentLifeStyleBinding -> {
                binding.listLayout.spinnerSelectRegion.selectItemByIndex(regionCode)
            }

            is FragmentMusicBinding -> {
                binding.listLayout.spinnerSelectRegion.selectItemByIndex(regionCode)
            }

            is FragmentSportsBinding -> {
                binding.listLayout.spinnerSelectRegion.selectItemByIndex(regionCode)
            }

            is FragmentMovieBinding -> {
                binding.listLayout.spinnerSelectRegion.selectItemByIndex(regionCode)
            }
        }
    }
}

fun bottomNavControl(currentId: Int, navView : View, homeBtn : View){
    if (currentId == R.id.end){
        navView.visibility = View.VISIBLE
        homeBtn.visibility = View.VISIBLE
    }else if(currentId == R.id.start){
        navView.visibility = View.GONE
        homeBtn.visibility = View.GONE
    }else{
        Log.d("MotionLayout","잘못된 상태 $currentId")
    }
}