package com.teamwable.network.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseInformationDto(
    @SerialName("infoNotificationType")
    val infoNotificationType: String = "",
    @SerialName("time")
    val time: String = "",
    @SerialName("imageUrl")
    val imageUrl: String = "",
    @SerialName("infoNotificationId")
    val infoNotificationId: Long = -1,
)
