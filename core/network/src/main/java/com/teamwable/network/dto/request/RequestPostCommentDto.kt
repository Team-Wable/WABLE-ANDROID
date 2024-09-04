package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostCommentDto(
    @SerialName("commentText") val commentText: String,
    @SerialName("notificationTriggerType") val notificationTriggerType: String,
)
