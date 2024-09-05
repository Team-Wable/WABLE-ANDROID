package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostCommentLikeDto(
    @SerialName("notificationTriggerType") val notificationTriggerType: String,
    @SerialName("notificationText") val notificationText: String,
)
