package com.teamwable.model.network

data class Feed(
    val postAuthorId: Long,
    val postAuthorProfile: String,
    val postAuthorNickname: String,
    val feedId: Long,
    val title: String,
    val content: String,
    val uploadTime: String,
    val isPostAuthorGhost: Boolean,
    val postAuthorGhost: Int,
    val isLiked: Boolean,
    val likedNumber: String,
    val commentNumber: String,
    val image: String,
    val postAuthorTeamTag: String,
)
