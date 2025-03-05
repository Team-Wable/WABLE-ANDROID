package com.teamwable.data.mapper.toData

import com.teamwable.model.viewit.LinkInfo
import com.teamwable.network.dto.request.viewit.RequestPostViewItDto

internal fun LinkInfo.toPostViewItDto(): RequestPostViewItDto =
    RequestPostViewItDto(
        this.linkImage,
        this.link,
        this.linkTitle,
        this.viewItText,
        this.linkName,
    )
