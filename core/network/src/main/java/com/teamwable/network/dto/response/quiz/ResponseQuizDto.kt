package com.teamwable.network.dto.response.quiz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseQuizDto(
    @SerialName("quizId")
    val quizId: Int = -1,
    @SerialName("quizImage")
    val quizImage: String = "",
    @SerialName("quizText")
    val quizText: String = "",
    @SerialName("quizAnswer")
    val quizAnswer: Boolean = false,
)
