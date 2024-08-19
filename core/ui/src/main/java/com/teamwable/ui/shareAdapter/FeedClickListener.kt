package com.teamwable.ui.shareAdapter

interface FeedClickListener {
    fun onItemClick(id: Long)

    fun onGhostBtnClick(postAuthorId: Long)

    fun onLikeBtnClick(id: Long)

    fun onPostAuthorProfileClick(id: Long)

    fun onFeedImageClick(image: String)

    fun onKebabBtnClick(feedId: Long, postAuthorId: Long)
}
