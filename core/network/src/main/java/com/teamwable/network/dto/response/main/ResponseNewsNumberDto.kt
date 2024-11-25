package com.teamwable.network.dto.response.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNewsNumberDto(
    @SerialName("newsNumber")
    val newsNumber: Int = 0,
    @SerialName("noticeNumber")
    val noticeNumber: Int = 0
)
