package com.teamwable.model

data class Comment(
    val commentId: Long,
    val postAuthorId: Long,
    val postAuthorProfile: String,
    val postAuthorNickname: String,
    val isPostAuthorGhost: Boolean,
    val postAuthorGhost: String,
    val isLiked: Boolean,
    val likedNumber: String,
    val content: String,
    val uploadTime: String,
    val postAuthorTeamTag: String,
    val feedId: Long?,
    val isBlind: Boolean,
    val parentCommentId: Long,
    val ghostColor: String = GhostColor.DEFAULT_0,
    val isAuth: Boolean = false,
)
