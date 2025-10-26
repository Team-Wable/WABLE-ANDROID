package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toQuizModel
import com.teamwable.data.mapper.toModel.toQuizResultModel
import com.teamwable.data.mapper.toModel.toRequestQuizScoreDto
import com.teamwable.data.repository.QuizRepository
import com.teamwable.data.util.runHandledCatching
import com.teamwable.model.quiz.QuizModel
import com.teamwable.model.quiz.QuizResultModel
import com.teamwable.model.quiz.QuizScoreModel
import com.teamwable.network.datasource.QuizService
import javax.inject.Inject

internal class DefaultQuizRepository @Inject constructor(
    private val quizService: QuizService,
) : QuizRepository {
    override suspend fun getQuiz(): Result<QuizModel> = runHandledCatching {
        quizService.getQuiz().data.toQuizModel()
    }

    override suspend fun patchQuizScore(quizScoreModel: QuizScoreModel): Result<QuizResultModel> = runHandledCatching {
        quizService.patchQuizScore(quizScoreModel.toRequestQuizScoreDto())
            .data
            .toQuizResultModel()
    }
}
