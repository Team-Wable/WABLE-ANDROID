package com.teamwable.model.home

data class Ghost(
    val alarmTriggerType: String,
    val postAuthorId: Long,
    val postId: Long,
    val reason: String = "(없음)",
)
