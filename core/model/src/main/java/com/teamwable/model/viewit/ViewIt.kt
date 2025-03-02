package com.teamwable.model.viewit

data class ViewIt(
    val postAuthorId: Long,
    val postAuthorProfile: String,
    val postAuthorNickname: String,
    val viewItId: Long,
    val linkImage: String,
    val linkName: String,
    val linkTitle: String,
    val viewItContent: String,
    val isLiked: Boolean,
    val likedNumber: String,
    val isBlind: Boolean,
)
