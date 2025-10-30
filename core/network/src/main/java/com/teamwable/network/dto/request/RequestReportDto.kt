package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestReportDto(
    @SerialName("reportTargetNickname") val reportTargetNickname: String,
    @SerialName("relateText") val relateText: String,
)
