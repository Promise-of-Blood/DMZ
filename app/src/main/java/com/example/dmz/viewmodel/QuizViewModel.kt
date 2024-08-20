package com.example.dmz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel: ViewModel() {
    private val _answer = MutableLiveData<Map<Int, Int>>()
    val answer : LiveData<Map<Int, Int>> = _answer

    fun submitAnswer(questionNumber: Int, selectedAnswer: Int) {
        _answer.value = _answer.value.orEmpty() + (questionNumber to selectedAnswer)
    }

    fun getSubmittedAnswer(questionNumber: Int): Int {
        return _answer.value?.getOrDefault(questionNumber, -1) ?: -1
    }
}