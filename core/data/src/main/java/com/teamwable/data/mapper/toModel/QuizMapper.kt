package com.teamwable.data.mapper.toModel

import com.teamwable.model.quiz.QuizModel
import com.teamwable.model.quiz.QuizResultModel
import com.teamwable.model.quiz.QuizScoreModel
import com.teamwable.network.dto.request.RequestQuizScoreDto
import com.teamwable.network.dto.response.quiz.ResponseQuizDto
import com.teamwable.network.dto.response.quiz.ResponseQuizScoreDto

internal fun ResponseQuizDto.toQuizModel(): QuizModel = QuizModel(
    quizId = quizId,
    quizImage = quizImage,
    quizText = quizText,
    quizAnswer = quizAnswer,
)

internal fun ResponseQuizScoreDto.toQuizResultModel(): QuizResultModel = QuizResultModel(
    quizResult = quizResult,
    userPercent = userPercent,
    continueNumber = continueNumber,
)

internal fun QuizScoreModel.toRequestQuizScoreDto(): RequestQuizScoreDto = RequestQuizScoreDto(
    quizId = quizId,
    userAnswer = userAnswer,
    quizTime = quizTime,
)
