package com.teamwable.ui.shareAdapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.teamwable.model.home.Feed
import com.teamwable.ui.extensions.ItemDiffCallback

class FeedAdapter(private val feedClickListener: FeedClickListener) : PagingDataAdapter<Feed, FeedViewHolder>(feedDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder = FeedViewHolder.from(parent, feedClickListener)

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val feedDiffCallback = ItemDiffCallback<Feed>(
            onItemsTheSame = { old, new -> old.feedId == new.feedId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
