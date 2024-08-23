package com.teamwable.model

data class Profile(
    val id: Long,
    val nickName: String,
    val profileImg: String,
    val intro: String,
    val ghost: Int,
    val teamTag: String,
    val lckYears: Int,
    val level: Int,
)
