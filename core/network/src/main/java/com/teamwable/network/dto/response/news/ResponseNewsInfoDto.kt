package com.teamwable.network.dto.response.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNewsInfoDto(
    @SerialName("newsId")
    val newsId: Long,
    @SerialName("newsTitle")
    val newsTitle: String,
    @SerialName("newsText")
    val newsText: String,
    @SerialName("newsImage")
    val newsImage: String,
    @SerialName("time")
    val time: String,
)
