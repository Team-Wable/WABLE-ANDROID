package com.teamwable.ui.shareAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.Comment
import com.teamwable.ui.databinding.ItemCommentBinding
import com.teamwable.ui.extensions.load

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
    commentClickListener: CommentClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: Comment

    init {
        setupClickListener(itemView, binding.tvCommentContent) { commentClickListener.onItemClick(item.feedId ?: return@setupClickListener) }
        setupClickListener(binding.btnCommentGhost) { commentClickListener.onGhostBtnClick(item.postAuthorId, item.commentId) }
        setupClickListener(binding.btnCommentLike) { commentClickListener.onLikeBtnClick(item.commentId) }
        setupClickListener(binding.ivCommentProfileImg, binding.tvCommentNickname) { commentClickListener.onPostAuthorProfileClick(item.postAuthorId) }
        setupClickListener(binding.btnCommentMore) { commentClickListener.onKebabBtnClick(item) }
    }

    private fun setupClickListener(vararg views: View, action: () -> Unit) {
        views.forEach { view ->
            view.setOnClickListener {
                if (this::item.isInitialized) action()
            }
        }
    }

    fun bind(comment: Comment?) {
        item = comment ?: return
        with(binding) {
            ivCommentProfileImg.load(comment.postAuthorProfile)
            tvCommentNickname.text = comment.postAuthorNickname
            tvCommentGhostLevel.text = comment.postAuthorGhost
            tvCommentUploadTime.text = comment.uploadTime
            tvCommentContent.text = comment.content
            btnCommentLike.isChecked = comment.isLiked
            tvCommentLikeCount.text = comment.likedNumber
            tvTeamTag.teamName = comment.postAuthorTeamTag
            btnCommentGhost.isEnabled = !comment.isPostAuthorGhost
            binding.viewCommentTransparentBg.setBackgroundColor(Color.parseColor(comment.ghostColor))
        }
    }

    companion object {
        fun from(parent: ViewGroup, commentClickListener: CommentClickListener): CommentViewHolder =
            CommentViewHolder(
                ItemCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                commentClickListener,
            )
    }
}
