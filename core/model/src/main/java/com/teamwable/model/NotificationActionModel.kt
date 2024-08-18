package com.teamwable.model

data class NotificationActionModel(
    val memberId: Int,
    val memberNickname: String,
    val triggerMemberNickname: String,
    val triggerMemberProfileUrl: String,
    val notificationTriggerType: String,
    val time: String,
    val notificationTriggerId: Int,
    val notificationText: String,
    val isNotificationChecked: Boolean,
    val isDeleted: Boolean,
    val notificationId: Int,
    val triggerMemberId: Int,
)
