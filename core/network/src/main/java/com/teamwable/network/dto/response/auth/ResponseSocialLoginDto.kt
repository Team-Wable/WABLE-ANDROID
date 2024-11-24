package com.teamwable.network.dto.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSocialLoginDto(
    @SerialName("nickName")
    val nickName: String,
    @SerialName("memberId")
    val memberId: Int,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("memberProfileUrl")
    val memberProfileUrl: String,
    @SerialName("isNewUser")
    val isNewUser: Boolean,
    @SerialName("isPushAlarmAllowed")
    val isPushAlarmAllowed: Boolean?,
    @SerialName("memberFanTeam")
    val memberFanTeam: String,
    @SerialName("memberLckYears")
    val memberLckYears: Int,
    @SerialName("memberLevel")
    val memberLevel: Int,
    @SerialName("isAdmin")
    val isAdmin: Boolean,
)
