package com.teamwable.community.model

import com.teamwable.common.type.LckTeamType
import com.teamwable.model.community.CommunityModel

internal fun CommunityModel.toLckTeamType(): LckTeamType? =
    LckTeamType.entries.find { it.name == this.communityName }
