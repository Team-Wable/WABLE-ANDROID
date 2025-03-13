package com.teamwable.community.model

import com.teamwable.common.type.LckTeamType
import com.teamwable.model.community.CommunityModel

internal fun CommunityModel.copyToLckTeamImage(): CommunityModel {
    val teamType = LckTeamType.entries.find { it.name == this.communityName }
    return this.copy(imageRes = teamType?.teamProfileImage ?: LckTeamType.T1.teamProfileImage)
}
