package com.teamwable.ui.shareAdapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.Feed
import com.teamwable.ui.databinding.ItemFeedBinding
import com.teamwable.ui.extensions.load
import com.teamwable.ui.extensions.visible

class FeedViewHolder private constructor(
    private val binding: ItemFeedBinding,
    feedClickListener: FeedClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: Feed

    init {
        setupClickListener(itemView, binding.tvFeedContent, binding.btnFeedComment) { feedClickListener.onItemClick(item) }
        setupClickListener(binding.btnFeedGhost) { feedClickListener.onGhostBtnClick(item.postAuthorId, item.feedId) }
        setupClickListener(binding.btnFeedLike) { feedClickListener.onLikeBtnClick(item.feedId) }
        setupClickListener(binding.ivFeedProfileImg, binding.tvFeedNickname) { feedClickListener.onPostAuthorProfileClick(item.postAuthorId) }
        setupClickListener(binding.ivFeedImg) { feedClickListener.onFeedImageClick(item.image) }
        setupClickListener(binding.btnFeedMore) { feedClickListener.onKebabBtnClick(item.feedId, item.postAuthorId) }
    }

    private fun setupClickListener(vararg views: View, action: () -> Unit) {
        views.forEach { view ->
            view.setOnClickListener {
                if (this::item.isInitialized) action()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(feed: Feed?) {
        item = feed ?: return
        val isImageInclude = feed.image.isNotBlank()
        with(binding) {
            ivFeedProfileImg.load(feed.postAuthorProfile)
            tvFeedNickname.text = feed.postAuthorNickname
            tvFeedGhostLevel.text = feed.postAuthorGhost.toString()
            tvFeedUploadTime.text = feed.uploadTime
            tvFeedTitle.text = feed.title
            tvFeedContent.text = feed.content
            ivFeedImg.apply {
                if (isImageInclude) load(feed.image)
                visible(isImageInclude)
            }
            btnFeedLike.isChecked = feed.isLiked
            tvFeedLikeCount.text = feed.likedNumber
            tvFeedCommentCount.text = feed.commentNumber
            tvTeamTag.teamName = feed.postAuthorTeamTag
            btnFeedGhost.isEnabled = !feed.isPostAuthorGhost
            binding.viewFeedTransparentBg.setBackgroundColor(Color.parseColor(feed.ghostColor))
        }
    }

    companion object {
        fun from(parent: ViewGroup, feedClickListener: FeedClickListener): FeedViewHolder =
            FeedViewHolder(
                ItemFeedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                feedClickListener,
            )
    }
}
