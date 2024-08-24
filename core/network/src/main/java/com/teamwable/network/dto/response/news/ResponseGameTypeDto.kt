package com.teamwable.network.dto.response.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGameTypeDto(
    @SerialName("lckGameType")
    val lckGameType: String = "",
)
