package com.teamwable.ui.shareAdapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.Comment
import com.teamwable.ui.R
import com.teamwable.ui.databinding.ItemCommentBinding
import com.teamwable.ui.extensions.load
import com.teamwable.ui.util.CalculateTime
import com.teamwable.ui.util.Transparent

class CommentViewHolder private constructor(
    private val binding: ItemCommentBinding,
    commentClickListener: CommentClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: Comment
    private var time: CalculateTime = CalculateTime()

    init {
        setupClickListener(binding.btnCommentGhost) {
            commentClickListener.onGhostBtnClick(item.postAuthorId, item.commentId)
            setCommentTransparent(item.postAuthorGhost)
        }
        setupClickListener(binding.btnCommentLike) { commentClickListener.onLikeBtnClick(item.commentId) }
        setupClickListener(binding.ivCommentProfileImg, binding.tvCommentNickname) { commentClickListener.onPostAuthorProfileClick(item.postAuthorId) }
        setupClickListener(binding.btnCommentMore) { commentClickListener.onKebabBtnClick(item.commentId, item.postAuthorId) }
    }

    private fun setupClickListener(vararg views: View, action: () -> Unit) {
        views.forEach { view ->
            view.setOnClickListener {
                if (this::item.isInitialized) action()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(comment: Comment?) {
        item = comment ?: return
        with(binding) {
            ivCommentProfileImg.load(comment.postAuthorProfile)
            tvCommentNickname.text = comment.postAuthorNickname
            tvCommentGhostLevel.text = itemView.context.getString(R.string.label_feed_ghost_level, comment.postAuthorGhost)
            tvCommentUploadTime.text = itemView.context.getString(R.string.label_feed_upload_time, time.getCalculateTime(itemView.context, comment.uploadTime))
            tvCommentContent.text = comment.content
            btnCommentLike.isChecked = comment.isLiked
            tvCommentLikeCount.text = comment.likedNumber
            tvTeamTag.teamName = comment.postAuthorTeamTag
            if (comment.isPostAuthorGhost) {
                btnCommentGhost.isEnabled = false
                setCommentTransparent(-85)
            } else {
                setCommentTransparent(comment.postAuthorGhost)
            }
        }
    }

    private fun setCommentTransparent(memberGhostPercent: Int) {
        val color = Transparent().calculateColorWithOpacity(memberGhostPercent)
        binding.viewCommentTransparentBg.setBackgroundColor(Color.parseColor(color))
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
