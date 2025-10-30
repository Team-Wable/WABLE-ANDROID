package com.teamwable.network.dto.response.news

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ResponseNewsCurationNumberDto(
    @SerialName("curationId") val curationId: Long,
)
