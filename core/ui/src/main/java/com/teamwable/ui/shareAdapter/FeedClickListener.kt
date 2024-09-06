package com.teamwable.ui.shareAdapter

import com.teamwable.model.Feed

interface FeedClickListener {
    fun onItemClick(feed: Feed)

    fun onGhostBtnClick(postAuthorId: Long, feedId: Long)

    fun onLikeBtnClick(viewHolder: FeedViewHolder, id: Long, isLiked: Boolean)

    fun onPostAuthorProfileClick(id: Long)

    fun onFeedImageClick(image: String)

    fun onKebabBtnClick(feed: Feed)

    fun onCommentBtnClick(feedId: Long)
}
