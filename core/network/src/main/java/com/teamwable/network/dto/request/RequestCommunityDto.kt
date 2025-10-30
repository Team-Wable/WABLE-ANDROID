package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCommunityDto(
    @SerialName("communityName") val communityName: String,
)
