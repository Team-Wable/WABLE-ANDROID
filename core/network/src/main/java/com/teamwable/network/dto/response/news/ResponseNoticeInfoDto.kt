package com.teamwable.network.dto.response.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNoticeInfoDto(
    @SerialName("noticeId")
    val noticeId: Long,
    @SerialName("noticeTitle")
    val noticeTitle: String,
    @SerialName("noticeText")
    val noticeText: String,
    @SerialName("noticeImage")
    val noticeImage: String?,
    @SerialName("time")
    val time: String,
)
