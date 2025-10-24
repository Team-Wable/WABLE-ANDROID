package com.teamwable.model.quiz

data class QuizScoreModel(
    val quizId: Long,
    val userAnswer: Boolean,
    val quizTime: Int,
)
