package com.teamwable.viewit.adapter

import com.teamwable.model.viewit.ViewIt

interface ViewItClickListener {
    fun onItemClick(link: String)

    fun onLikeBtnClick(viewHolder: ViewItViewHolder, id: Long, isLiked: Boolean)

    fun onPostAuthorProfileClick(id: Long)

    fun onKebabBtnClick(viewIt: ViewIt)
}
