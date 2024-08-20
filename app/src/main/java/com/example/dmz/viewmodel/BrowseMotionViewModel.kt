package com.example.dmz.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BrowseMotionViewModel : ViewModel(){
    val _isListVisible = MutableLiveData<Boolean>()
    val isListVisible get() = _isListVisible

    fun setListVisible(visible : Boolean){
        _isListVisible.value = visible
    }
}