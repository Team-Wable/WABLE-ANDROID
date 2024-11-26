package com.teamwable.data.mapper.toData

import com.teamwable.network.dto.request.RequestPostCommentDto

internal fun Triple<String, Long, Long>.toPostCommentDto(): RequestPostCommentDto =
    RequestPostCommentDto(commentText = first, parentCommentId = second, parentCommentWriterId = third)
