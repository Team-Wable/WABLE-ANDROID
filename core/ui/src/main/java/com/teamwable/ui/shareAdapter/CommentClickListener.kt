package com.teamwable.ui.shareAdapter

interface CommentClickListener {
    fun onGhostBtnClick(postAuthorId: Long)

    fun onLikeBtnClick(id: Long)

    fun onPostAuthorProfileClick(id: Long)

    fun onKebabBtnClick(feedId: Long, postAuthorId: Long)
}
