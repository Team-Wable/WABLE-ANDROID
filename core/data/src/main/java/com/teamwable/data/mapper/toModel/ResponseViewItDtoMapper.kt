package com.teamwable.data.mapper.toModel

import com.teamwable.model.viewit.ViewIt
import com.teamwable.network.dto.response.viewit.ResponseViewItDto

internal fun ResponseViewItDto.toViewIt(): ViewIt = ViewIt(
    this.memberId,
    this.memberProfileUrl,
    this.memberNickname,
    this.viewitId,
    this.viewitImage,
    this.viewitLink,
    this.viewitTitle,
    this.viewitText,
    this.isLiked,
    this.likedNumber.toString(),
    this.isBlind,
)
