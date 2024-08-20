package com.example.dmz.data

import com.example.dmz.data.model.Question
import com.example.dmz.data.model.Quiz
import java.time.LocalDate


fun quizList(): List<Quiz> {
    val keywordList = CacheDataSource.getCacheDataSource().getUserList()

    return listOf(
        Quiz(
            date = LocalDate.of(2024, 8, 20),
            keyword = keywordList.random(),
            questions = listOf(
                Question(
                    question = "질문1",
                    answers = listOf("답안1", "답안2", "답안3"),
                    correctAnswerIndex = 2
                ),
                Question(
                    question = "질문2",
                    answers = listOf("답안1", "답안2", "답안3"),
                    correctAnswerIndex = 1
                ),
                Question(
                    question = "질문3",
                    answers = listOf("답안1", "답안2", "답안3"),
                    correctAnswerIndex = 1
                ),
            )
        ),
        Quiz(
            date = LocalDate.of(2024, 8, 21),
            keyword = keywordList.random(),
            questions = listOf(
                Question(
                    question = "질문1",
                    answers = listOf("답안1", "답안2", "답안3"),
                    correctAnswerIndex = 0
                ),
                Question(
                    question = "질문2",
                    answers = listOf("답안1", "답안2", "답안3"),
                    correctAnswerIndex = 2
                ),
                Question(
                    question = "질문3",
                    answers = listOf("답안1", "답안2", "답안3"),
                    correctAnswerIndex = 1
                ),
            )
        ),
    )
}