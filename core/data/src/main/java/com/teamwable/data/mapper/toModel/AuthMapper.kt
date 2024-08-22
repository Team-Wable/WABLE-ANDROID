package com.teamwable.data.mapper.toModel

import com.teamwable.model.auth.UserModel
import com.teamwable.network.dto.response.auth.ResponseSocialLoginDto

internal fun ResponseSocialLoginDto.toUserModel(): UserModel =
    UserModel(
        nickName = nickName,
        memberId = memberId,
        accessToken = accessToken,
        refreshToken = refreshToken,
        memberProfileUrl = memberProfileUrl,
        isNewUser = isNewUser,
        isPushAlarmAllowed = isPushAlarmAllowed,
        memberFanTeam = memberFanTeam,
        memberLckYears = memberLckYears,
        memberLevel = memberLevel
    )
