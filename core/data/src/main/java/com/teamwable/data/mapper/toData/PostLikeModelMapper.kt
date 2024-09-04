package com.teamwable.data.mapper.toData

import com.teamwable.network.dto.request.RequestPostLikeDto

internal fun String.toPostLikeDto(): RequestPostLikeDto =
    RequestPostLikeDto(this)
