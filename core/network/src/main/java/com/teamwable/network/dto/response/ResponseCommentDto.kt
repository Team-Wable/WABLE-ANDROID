package com.teamwable.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCommentDto(
    @SerialName("commentId") val commentId: Long,
    @SerialName("memberId") val memberId: Long,
    @SerialName("memberProfileUrl") val memberProfileUrl: String,
    @SerialName("memberNickname") val memberNickname: String,
    @SerialName("isGhost") val isGhost: Boolean,
    @SerialName("memberGhost") val memberGhost: Int,
    @SerialName("isLiked") val isLiked: Boolean,
    @SerialName("commentLikedNumber") val commentLikedNumber: Int,
    @SerialName("commentText") val commentText: String,
    @SerialName("time") val time: String,
    @SerialName("isDeleted") val isDeleted: Boolean? = null,
    @SerialName("commentImageUrl") val commentImageUrl: String? = null,
    @SerialName("memberFanTeam") val memberFanTeam: String,
    @SerialName("contentId") val contentId: Long? = null,
)
