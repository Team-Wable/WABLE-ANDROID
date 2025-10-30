package com.teamwable.data.mapper.toData

import com.teamwable.network.dto.request.RequestPostCommentLikeDto
import com.teamwable.network.dto.request.RequestPostFeedLikeDto

internal fun String.toPostFeedLikeDto(): RequestPostFeedLikeDto =
    RequestPostFeedLikeDto(this)

internal fun Pair<String, String>.toPostCommentLikeDto(): RequestPostCommentLikeDto =
    RequestPostCommentLikeDto(first, second)
