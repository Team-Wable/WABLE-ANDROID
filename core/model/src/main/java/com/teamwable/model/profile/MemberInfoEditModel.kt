package com.teamwable.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MemberInfoEditModel(
    val nickname: String? = null,
    val isAlarmAllowed: Boolean? = null,
    val memberIntro: String? = null,
    val isPushAlarmAllowed: Boolean? = null,
    val fcmToken: String? = null,
    val memberLckYears: Int? = null,
    val memberFanTeam: String? = null,
    val memberDefaultProfileImage: String? = null,
) : Parcelable
