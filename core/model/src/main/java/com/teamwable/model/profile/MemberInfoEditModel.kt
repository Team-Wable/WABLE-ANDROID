package com.teamwable.model.profile

data class MemberInfoEditModel(
    val nickname: String? = "",
    val isAlarmAllowed: Boolean? = false,
    val memberIntro: String? = "",
    val isPushAlarmAllowed: Boolean? = false,
    val fcmToken: String? = "",
    val memberLckYears: Int = 0,
    val memberFanTeam: String? = "",
    val memberDefaultProfileImage: String? = ""
)
