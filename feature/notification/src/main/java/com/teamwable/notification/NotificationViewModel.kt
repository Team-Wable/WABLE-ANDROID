package com.teamwable.notification

import androidx.lifecycle.ViewModel
import com.teamwable.model.NotificationActionModel
import com.teamwable.model.NotificationInformationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
@Inject constructor() : ViewModel() {
    val mockNotificationEmptyList = emptyList<Any>()

    val mockNotificationActionList = listOf(
        NotificationActionModel(0, "배차은우", "배차은우", "", "contentLiked", "2024-08-18 00:00:00", 0, "안녕? 난 차은우보다 잘생긴 배 차은우 하하", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "comment", "2024-08-17 06:50:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "commentLiked", "2024-08-17 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "actingContinue", "2024-08-16 20:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "beGhost", "2024-08-16 18:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "contentGhost", "2024-08-14 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "commentGhost", "2024-08-13 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "userBan", "2024-08-13 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
        NotificationActionModel(0, "배차은우", "배차은우", "", "popularWriter", "2024-08-12 00:00:00", 0, "안녕? 난 배 차은우", false, false, 1, 0),
    )

    val mockNotificationInformationList = listOf(
        NotificationInformationModel("GAMEDONE", "2024-08-18 00:00:00", ""),
        NotificationInformationModel("GAMESTART", "2024-08-18 00:00:00", ""),
        NotificationInformationModel("WEEKDONE", "2024-08-18 00:00:00", ""),
    )
}
