package com.teamwable.network.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNotificationsDto(
    @SerialName("memberId")
    val memberId: Int = -1,
    @SerialName("memberNickname")
    val memberNickname: String = "",
    @SerialName("triggerMemberNickname")
    val triggerMemberNickname: String = "",
    @SerialName("triggerMemberProfileUrl")
    val triggerMemberProfileUrl: String = "",
    @SerialName("notificationTriggerType")
    val notificationTriggerType: String = "",
    @SerialName("time")
    val time: String = "",
    @SerialName("notificationTriggerId")
    val notificationTriggerId: Int = -1,
    @SerialName("notificationText")
    val notificationText: String? = "",
    @SerialName("isNotificationChecked")
    val isNotificationChecked: Boolean = false,
    @SerialName("isDeleted")
    val isDeleted: Boolean = false,
    @SerialName("notificationId")
    val notificationId: Long = -1,
    @SerialName("triggerMemberId")
    val triggerMemberId: Int = -1,
)
