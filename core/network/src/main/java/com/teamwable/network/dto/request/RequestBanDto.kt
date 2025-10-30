package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestBanDto(
    @SerialName("memberId") val memberId: Long,
    @SerialName("triggerType") val triggerType: String,
    @SerialName("triggerId") val triggerId: Long,
)
