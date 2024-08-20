package com.example.dmz.data.repository

import com.example.dmz.data.model.Quiz

interface QuizRepository {
    fun getTodayQuiz(): Quiz
    fun getTodayQuizAnswer(): List<Int>
}