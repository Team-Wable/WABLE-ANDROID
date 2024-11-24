package com.teamwable.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feed(
    val postAuthorId: Long = -1,
    val postAuthorProfile: String = "",
    val postAuthorNickname: String = "",
    val feedId: Long = -1,
    val title: String = "",
    val content: String = "",
    val uploadTime: String = "",
    val isPostAuthorGhost: Boolean = false,
    val postAuthorGhost: String = "",
    val isLiked: Boolean = false,
    val likedNumber: String = "",
    val commentNumber: String = "",
    val image: String = "",
    val postAuthorTeamTag: String = "",
    val ghostColor: String = GhostColor.DEFAULT_0,
    val isAuth: Boolean = false,
    val isBlind: Boolean = false,
) : Parcelable

object GhostColor {
    const val DEFAULT_0 = "#00FCFCFD"
    const val MINUS_85 = "#D8FCFCFD"
}
