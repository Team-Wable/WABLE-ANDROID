package com.teamwable.network.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNotificationNumberDto(
    @SerialName("notificationNumber")
    val notificationNumber: Int = 0
)
