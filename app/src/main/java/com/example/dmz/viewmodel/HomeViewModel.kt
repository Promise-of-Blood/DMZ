package com.example.dmz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.model.Keywords
import com.example.dmz.data.repository.KeywordsRepository
import com.example.dmz.data.repository.KeywordsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomeViewModel(private val keywordsRepository: KeywordsRepository) : ViewModel() {

    private val _keywordsList = MutableStateFlow<List<Keywords>>(emptyList())
    val keywordsList: StateFlow<List<Keywords>> = _keywordsList.asStateFlow()

    fun getKeywordsList() {
        viewModelScope.launch {
            keywordsRepository.getKeywordsList()
                .flowOn(Dispatchers.IO)
                .catch { exception ->
                    Log.e("HomeViewModel", "에러: $exception")
                    _keywordsList.value = emptyList()
                }
                .collect { value ->
                    Log.d("HomeViewModel", "출력값: $value")
                    _keywordsList.value = value
                }
        }
    }
}

class HomeViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val repository = KeywordsRepositoryImpl(CacheDataSource.getCacheDataSource())
        return HomeViewModel(
            keywordsRepository = repository
        ) as T
    }
}
