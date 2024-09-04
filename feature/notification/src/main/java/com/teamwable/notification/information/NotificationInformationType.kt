package com.teamwable.notification.information

import androidx.annotation.StringRes

enum class NotificationInformationType(@StringRes val content: Int) {
    GAMEDONE(com.teamwable.notification.R.string.tv_notification_information_game_done),
    GAMESTART(com.teamwable.notification.R.string.tv_notification_information_game_start),
    WEEKDONE(com.teamwable.notification.R.string.tv_notification_information_week_done),
}
