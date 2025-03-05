package com.teamwable.data.mapper.toModel

import com.teamwable.model.community.CommunityModel
import com.teamwable.network.dto.response.community.ResponseCommunityInfoDto

internal fun ResponseCommunityInfoDto.toCommunityModel(): CommunityModel = CommunityModel(
    communityName = communityName,
    communityNum = communityNum.toFloat(),
)
