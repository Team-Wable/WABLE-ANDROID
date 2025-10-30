package com.teamwable.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileInfoDto(
    @SerialName("memberId") val memberId: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("memberProfileUrl") val memberProfileUrl: String,
    @SerialName("memberIntro") val memberIntro: String,
    @SerialName("memberGhost") val memberGhost: Int,
    @SerialName("memberFanTeam") val memberFanTeam: String,
    @SerialName("memberLckYears") val memberLckYears: Int,
    @SerialName("memberLevel") val memberLevel: Int,
)
