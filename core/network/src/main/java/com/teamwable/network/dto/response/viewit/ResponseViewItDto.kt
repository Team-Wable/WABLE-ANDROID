package com.teamwable.network.dto.response.viewit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseViewItDto(
    @SerialName("memberId") val memberId: Long,
    @SerialName("memberProfileUrl") val memberProfileUrl: String,
    @SerialName("memberNickname") val memberNickname: String,
    @SerialName("viewitId") val viewitId: Long,
    @SerialName("viewitImage") val viewitImage: String,
    @SerialName("viewitLink") val viewitLink: String,
    @SerialName("viewitTitle") val viewitTitle: String,
    @SerialName("viewitText") val viewitText: String,
    @SerialName("time") val time: String,
    @SerialName("isLiked") val isLiked: Boolean,
    @SerialName("likedNumber") val likedNumber: Int,
    @SerialName("isBlind") val isBlind: Boolean,
)
