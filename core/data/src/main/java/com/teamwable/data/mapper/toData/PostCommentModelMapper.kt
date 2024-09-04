package com.teamwable.data.mapper.toData

import com.teamwable.network.dto.request.RequestPostCommentDto

internal fun Pair<String, String>.toPostCommentDto(): RequestPostCommentDto =
    RequestPostCommentDto(first, second)
