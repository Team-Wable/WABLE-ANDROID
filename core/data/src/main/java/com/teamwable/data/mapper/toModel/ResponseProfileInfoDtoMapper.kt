package com.teamwable.data.mapper.toModel

import com.teamwable.model.Profile
import com.teamwable.network.dto.response.ResponseProfileInfoDto

internal fun ResponseProfileInfoDto.toProfile(): Profile =
    Profile(
        this.memberId,
        this.nickname,
        this.memberProfileUrl,
        this.memberIntro,
        this.memberGhost,
        this.memberFanTeam,
        this.memberLckYears,
        this.memberLevel,
    )
