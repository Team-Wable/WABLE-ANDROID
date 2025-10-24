package com.teamwable.model.quiz

data class QuizModel(
    val quizId: Int = -1,
    val quizImage: String = "",
    val quizText: String = "",
    val quizAnswer: Boolean = false,
)
