package com.teamwable.data.mapper.toData

import com.teamwable.model.Ghost
import com.teamwable.network.dto.request.RequestGhostDto

class PostGhostModelMapper

internal fun Ghost.toPostGhostDto(): RequestGhostDto =
    RequestGhostDto(
        this.alarmTriggerType,
        this.postAuthorId,
        this.postId,
        "null",
    )
