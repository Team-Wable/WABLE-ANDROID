package com.teamwable.ui.shareAdapter

import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.teamwable.model.Comment
import com.teamwable.ui.extensions.ItemDiffCallback

class CommentAdapter(private val commentClickListener: CommentClickListener) : PagingDataAdapter<Comment, CommentViewHolder>(commentDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder = CommentViewHolder.from(parent, commentClickListener)

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) = holder.bind(getItem(position))

    suspend fun removeComment(commentId: Long) {
        val currentList = snapshot().items.toMutableList()
        val indexToRemove = currentList.indexOfFirst { it.commentId == commentId }
        if (indexToRemove != -1) {
            currentList.removeAt(indexToRemove)
            submitData(PagingData.from(currentList))
        }
    }

    companion object {
        private val commentDiffCallback = ItemDiffCallback<Comment>(
            onItemsTheSame = { old, new -> old.commentId == new.commentId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
