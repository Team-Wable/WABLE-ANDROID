package com.teamwable.network.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNumberDto(
    @SerialName("notificationNumber")
    val notificationNumber: Int = 0
)
