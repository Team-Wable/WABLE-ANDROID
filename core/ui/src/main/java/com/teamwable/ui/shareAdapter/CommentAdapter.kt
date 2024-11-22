package com.teamwable.ui.shareAdapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.teamwable.model.Comment
import com.teamwable.ui.extensions.ItemDiffCallback

class CommentAdapter(private val commentClickListener: CommentClickListener) : PagingDataAdapter<Comment, ViewHolder>(commentDiffCallback) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.parentCommentId ?: PARENT_COMMENT_DEFAULT) {
            PARENT_COMMENT_DEFAULT -> VIEW_TYPE_COMMENT
            else -> VIEW_TYPE_CHILD_COMMENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COMMENT -> CommentViewHolder.from(parent, commentClickListener)
            else -> ChildCommentViewHolder.from(parent, commentClickListener)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        when (data?.parentCommentId ?: PARENT_COMMENT_DEFAULT) {
            PARENT_COMMENT_DEFAULT -> (holder as? CommentViewHolder)?.run { bind(data) }
            else -> (holder as? ChildCommentViewHolder)?.run { bind(data) }
        }
    }

    companion object {
        const val VIEW_TYPE_COMMENT = 0
        const val VIEW_TYPE_CHILD_COMMENT = 1
        const val PARENT_COMMENT_DEFAULT = -1L
        private val commentDiffCallback = ItemDiffCallback<Comment>(
            onItemsTheSame = { old, new -> old.commentId == new.commentId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
