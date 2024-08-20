package com.example.dmz.data.model

import java.time.LocalDate

data class Quiz(
    val date: LocalDate,
    val keyword: Keywords,
    val questions: List<Question>,
)

data class Question(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)
