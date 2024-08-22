package com.teamwable.data.mapper.toModel

import com.teamwable.model.NotificationActionModel
import com.teamwable.model.NotificationInformationModel
import com.teamwable.network.dto.response.notification.ResponseInformationDto
import com.teamwable.network.dto.response.notification.ResponseNotificationsDto

internal fun ResponseNotificationsDto.toNotificationActionModel(): NotificationActionModel =
    NotificationActionModel(
        memberId = memberId,
        memberNickname = memberNickname,
        triggerMemberNickname = triggerMemberNickname,
        triggerMemberProfileUrl = triggerMemberProfileUrl,
        notificationTriggerType = notificationTriggerType,
        time = time,
        notificationTriggerId = notificationTriggerId,
        notificationText = notificationText ?: "",
        isNotificationChecked = isNotificationChecked,
        isDeleted = isDeleted,
        notificationId = notificationId,
        triggerMemberId = triggerMemberId
    )

internal fun ResponseInformationDto.toNotificationInformationModel(): NotificationInformationModel =
    NotificationInformationModel(
        infoNotificationType = infoNotificationType,
        time = time,
        imageUrl = imageUrl
    )
