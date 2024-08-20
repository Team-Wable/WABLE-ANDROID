package com.teamwable.model.auth

data class UserModel(
    val nickName: String,
    val memberId: Int,
    val accessToken: String,
    val refreshToken: String,
    val memberProfileUrl: String,
    val isNewUser: Boolean,
    val isPushAlarmAllowed: Any?,
    val memberFanTeam: String,
    val memberLckYears: Int,
    val memberLevel: Int,
)
