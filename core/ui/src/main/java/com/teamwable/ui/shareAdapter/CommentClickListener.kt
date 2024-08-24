package com.teamwable.ui.shareAdapter

interface CommentClickListener {
    fun onGhostBtnClick(postAuthorId: Long, commentId: Long)

    fun onLikeBtnClick(id: Long)

    fun onPostAuthorProfileClick(id: Long)

    fun onKebabBtnClick(feedId: Long, postAuthorId: Long)
}
