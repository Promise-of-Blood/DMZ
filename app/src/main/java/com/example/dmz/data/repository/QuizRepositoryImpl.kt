package com.example.dmz.data.repository

import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.model.Quiz

class QuizRepositoryImpl : QuizRepository {
    override fun getTodayQuiz(): Quiz {
        return quizList.first()
    }

    override fun getTodayQuizAnswer(): List<Int> {
        return getTodayQuiz().questions.map { it.correctAnswerIndex }
    }

    fun shuffleQuizList() {
        quizList = quizList.shuffled()
    }

    companion object {
        private var quizList = CacheDataSource.getCacheDataSource().getQuizList()
    }
}