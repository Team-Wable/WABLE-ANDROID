package com.teamwable.data.repository

import com.teamwable.model.quiz.QuizModel
import com.teamwable.model.quiz.QuizResultModel
import com.teamwable.model.quiz.QuizScoreModel

interface QuizRepository {
    suspend fun getQuiz(): Result<QuizModel>

    suspend fun patchQuizScore(quizScoreModel: QuizScoreModel): Result<QuizResultModel>
}
