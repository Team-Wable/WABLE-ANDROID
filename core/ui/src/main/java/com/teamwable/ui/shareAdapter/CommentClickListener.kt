package com.teamwable.ui.shareAdapter

import com.teamwable.model.Comment

interface CommentClickListener {
    fun onGhostBtnClick(postAuthorId: Long, commentId: Long)

    fun onLikeBtnClick(viewHolder: CommentViewHolder, comment: Comment)

    fun onPostAuthorProfileClick(id: Long)

    fun onKebabBtnClick(comment: Comment)

    fun onItemClick(feedId: Long)
}
