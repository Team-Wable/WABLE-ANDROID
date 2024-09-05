package com.teamwable.ui.shareAdapter

import com.teamwable.model.Comment

interface CommentClickListener {
    fun onGhostBtnClick(postAuthorId: Long, commentId: Long)

    fun onLikeBtnClick(id: Long)

    fun onPostAuthorProfileClick(id: Long)

    fun onKebabBtnClick(comment: Comment)

    fun onItemClick(feedId: Long)
}
