package com.teamwable.network.dto.response.community

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseJoinedCommunityDto(
    @SerialName("community")
    val community: String?,
)
