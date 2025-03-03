package com.teamwable.ui.shareAdapter

import android.widget.CheckBox
import android.widget.TextView
import com.teamwable.model.home.Comment

interface CommentClickListener {
    fun onGhostBtnClick(postAuthorId: Long, commentId: Long)

    fun onLikeBtnClick(viewHolder: LikeableViewHolder, comment: Comment)

    fun onPostAuthorProfileClick(id: Long)

    fun onKebabBtnClick(comment: Comment)

    fun onItemClick(feedId: Long)

    fun onChildCommentClick(comment: Comment)
}

interface LikeableViewHolder {
    val likeBtn: CheckBox
    val likeCountTv: TextView
}
