package com.teamwable.network.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMemberDataDto(
    @SerialName("memberId")
    val memberId: Int = -1,
    @SerialName("joinDate")
    val joinDate: String = "",
    @SerialName("showMemberId")
    val showMemberId: String = "",
    @SerialName("socialPlatform")
    val socialPlatform: String = "",
    @SerialName("versionInformation")
    val versionInformation: String = "",
)
