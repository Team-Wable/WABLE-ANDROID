package com.teamwable.model.home

data class LikeState(
    val isLiked: Boolean,
    val count: String,
) {
    fun toggle(): LikeState = copy(
        isLiked = !isLiked,
        count = (count.toInt() + if (isLiked) -1 else 1).toString(),
    )
}
