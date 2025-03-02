package com.teamwable.viewit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.extensions.load
import com.teamwable.viewit.databinding.ItemViewItBinding

class ViewItViewHolder private constructor(
    private val binding: ItemViewItBinding,
    viewItClickListener: ViewItClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: ViewIt

    init {
        setupClickListener(binding.cvViewItLink) { viewItClickListener.onItemClick(item.linkName) }
        setupClickListener(binding.btnViewItLike) { viewItClickListener.onLikeBtnClick(this, item.viewItId, item.isLiked) }
        setupClickListener(binding.ivViewItProfileImg, binding.tvViewItNickname) { viewItClickListener.onPostAuthorProfileClick(item.postAuthorId) }
        setupClickListener(binding.btnViewItMore) { viewItClickListener.onKebabBtnClick(item) }
    }

    private fun setupClickListener(vararg views: View, action: () -> Unit) {
        views.forEach { view ->
            view.setOnClickListener {
                if (this::item.isInitialized) action()
            }
        }
    }

    fun bind(viewIt: ViewIt?) = with(binding) {
        item = viewIt ?: return
        ivViewItProfileImg.load(item.postAuthorProfile)
        tvViewItNickname.text = item.postAuthorNickname
        tvViewItContent.text = item.viewItContent
        ivViewItLinkImg.load(item.linkImage)
        tvViewItLinkTitle.text = item.linkTitle
        tvViewItLinkName.text = item.linkName
        btnViewItLike.isChecked = item.isLiked
        tvViewItLikeCount.text = item.likedNumber
    }

    companion object {
        fun from(parent: ViewGroup, viewItClickListener: ViewItClickListener): ViewItViewHolder =
            ViewItViewHolder(
                ItemViewItBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                viewItClickListener,
            )
    }
}
