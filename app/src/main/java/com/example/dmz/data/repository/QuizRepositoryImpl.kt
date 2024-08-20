package com.example.dmz.data.repository

import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.model.Quiz
import java.time.LocalDate

class QuizRepositoryImpl(private val cacheDataSource: CacheDataSource) : QuizRepository {
    override fun getTodayQuiz(): Quiz {
        val today = LocalDate.now()
        val quizList = cacheDataSource.getQuizList()
        return quizList.firstOrNull { it.date == today } ?: quizList[0]
    }

    override fun getTodayQuizAnswer(): List<Int> {
        return getTodayQuiz().questions.map { it.correctAnswerIndex }
    }
}