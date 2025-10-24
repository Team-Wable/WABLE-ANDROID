package com.teamwable.network.dto.response.news

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ResponseCurationInfoDto(
    @SerialName("curationId") val curationId: Long,
    @SerialName("curationLink") val curationLink: String,
    @SerialName("curationTitle") val curationTitle: String?,
    @SerialName("curationThumbnail") val curationThumbnail: String?,
    @SerialName("time") val time: String,
)
