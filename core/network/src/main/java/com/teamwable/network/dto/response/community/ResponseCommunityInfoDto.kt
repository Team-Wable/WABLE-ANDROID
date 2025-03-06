package com.teamwable.network.dto.response.community

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCommunityInfoDto(
    @SerialName("communityName")
    val communityName: String,
    @SerialName("communityNum")
    val communityNum: Double,
)
