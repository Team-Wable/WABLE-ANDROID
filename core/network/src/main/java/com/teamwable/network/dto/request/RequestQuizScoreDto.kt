package com.teamwable.network.dto.request

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class RequestQuizScoreDto(
    @SerialName("quizId") val quizId: Long,
    @SerialName("userAnswer") val userAnswer: Boolean,
    @SerialName("quizTime") val quizTime: Int,
)
