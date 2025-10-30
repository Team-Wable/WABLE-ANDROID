package com.teamwable.network.datasource

import com.teamwable.network.dto.request.RequestQuizScoreDto
import com.teamwable.network.dto.response.quiz.ResponseQuizDto
import com.teamwable.network.dto.response.quiz.ResponseQuizScoreDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface QuizService {
    @GET("api/v1/quiz")
    suspend fun getQuiz(): BaseResponse<ResponseQuizDto>

    @PATCH("api/v1/quiz/grade")
    suspend fun patchQuizScore(
        @Body requestQuizScoreDto: RequestQuizScoreDto,
    ): BaseResponse<ResponseQuizScoreDto>
}
