package com.teamwable.network.dto.response.quiz

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ResponseQuizScoreDto(
    @SerialName("quizResult")
    val quizResult: Boolean = false,
    @SerialName("userPercent")
    val userPercent: Int = 0,
    @SerialName("continueNumber")
    val continueNumber: Int = 0,
)
