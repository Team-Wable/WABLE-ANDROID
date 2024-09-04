package com.teamwable.data.mapper.toModel

import com.teamwable.model.profile.MemberDataModel
import com.teamwable.network.dto.response.profile.ResponseMemberDataDto

internal fun ResponseMemberDataDto.toMemberDataModel(): MemberDataModel =
    MemberDataModel(
        joinDate = joinDate,
        showMemberId = showMemberId,
        socialPlatform = socialPlatform,
        versionInformation = versionInformation
    )
