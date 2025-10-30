package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostCommentDto(
    @SerialName("commentText") val commentText: String,
    @SerialName("parentCommentId") val parentCommentId: Long,
    @SerialName("parentCommentWriterId") val parentCommentWriterId: Long,
)
