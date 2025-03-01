package com.teamwable.viewit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.extensions.load
import com.teamwable.viewit.databinding.ItemViewItBinding

class ViewItViewHolder private constructor(
    private val binding: ItemViewItBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: ViewIt

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
        fun from(parent: ViewGroup): ViewItViewHolder =
            ViewItViewHolder(
                ItemViewItBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
    }
}
