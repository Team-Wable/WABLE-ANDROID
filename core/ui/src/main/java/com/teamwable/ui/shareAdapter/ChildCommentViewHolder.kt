package com.teamwable.ui.shareAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.Comment
import com.teamwable.ui.databinding.ItemChildCommentBinding
import com.teamwable.ui.extensions.load
import com.teamwable.ui.extensions.visible

class ChildCommentViewHolder private constructor(
    private val binding: ItemChildCommentBinding,
    commentClickListener: CommentClickListener,
) : RecyclerView.ViewHolder(binding.root), LikeableViewHolder {
    private lateinit var item: Comment
    override val likeBtn = binding.btnChildCommentLike
    override val likeCountTv = binding.tvChildCommentLikeCount

    init {
        setupClickListener(itemView, binding.tvChildCommentContent) { commentClickListener.onItemClick(item.feedId ?: return@setupClickListener) }
        setupClickListener(binding.btnChildCommentGhost) { commentClickListener.onGhostBtnClick(item.postAuthorId, item.commentId) }
        setupClickListener(binding.btnChildCommentLike) { commentClickListener.onLikeBtnClick(this, item) }
        setupClickListener(binding.ivChildCommentProfileImg, binding.tvChildCommentNickname) { commentClickListener.onPostAuthorProfileClick(item.postAuthorId) }
        setupClickListener(binding.btnChildCommentMore) { commentClickListener.onKebabBtnClick(item) }
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
            ivChildCommentProfileImg.load(comment.postAuthorProfile)
            tvChildCommentNickname.text = comment.postAuthorNickname
            tvChildCommentGhostLevel.text = comment.postAuthorGhost
            tvChildCommentUploadTime.text = comment.uploadTime
            tvChildCommentContent.text = comment.content
            btnChildCommentLike.isChecked = comment.isLiked
            tvChildCommentLikeCount.text = comment.likedNumber
            tvTeamTag.teamName = comment.postAuthorTeamTag
            btnChildCommentGhost.isEnabled = !comment.isPostAuthorGhost
            viewChildCommentTransparentBg.setBackgroundColor(Color.parseColor(comment.ghostColor))
            btnChildCommentGhost.visible(!comment.isAuth)
            spacerChildComment.visible(!comment.isAuth)
            setBlindVisible(comment.isBlind)
        }
    }

    private fun setBlindVisible(isBlind: Boolean) = with(binding) {
        tvChildCommentContent.visible(!isBlind)
        tvChildCommentBlind.visible(isBlind)
    }

    companion object {
        fun from(parent: ViewGroup, commentClickListener: CommentClickListener): ChildCommentViewHolder =
            ChildCommentViewHolder(
                ItemChildCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                commentClickListener,
            )
    }
}
