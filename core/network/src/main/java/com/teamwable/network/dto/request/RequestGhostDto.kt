package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestGhostDto(
    @SerialName("alarmTriggerType") val alarmTriggerType: String,
    @SerialName("targetMemberId") val targetMemberId: Long,
    @SerialName("alarmTriggerId") val alarmTriggerId: Long,
    @SerialName("ghostReason") val ghostReason: String,
)
