package com.teamwable.network.dto.request.viewit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostViewItDto(
    @SerialName("viewitImage") val viewitImage: String,
    @SerialName("viewitLink") val viewitLink: String,
    @SerialName("viewitTitle") val viewitTitle: String,
    @SerialName("viewitText") val viewitText: String,
    @SerialName("viewitName") val viewitName: String,
)
