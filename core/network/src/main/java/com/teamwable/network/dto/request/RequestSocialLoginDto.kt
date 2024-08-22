package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestSocialLoginDto(
    @SerialName("socialPlatform")
    val socialPlatform: String,
)
