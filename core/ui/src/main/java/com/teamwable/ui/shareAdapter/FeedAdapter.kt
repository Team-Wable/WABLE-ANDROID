package com.teamwable.ui.shareAdapter

import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.teamwable.model.Feed
import com.teamwable.ui.extensions.ItemDiffCallback

class FeedAdapter(private val feedClickListener: FeedClickListener) : PagingDataAdapter<Feed, FeedViewHolder>(feedDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder = FeedViewHolder.from(parent, feedClickListener)

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) = holder.bind(getItem(position))

    suspend fun removeFeed(feedId: Long) {
        val currentList = snapshot().items.toMutableList()
        val indexToRemove = currentList.indexOfFirst { it.feedId == feedId }
        if (indexToRemove != -1) {
            currentList.removeAt(indexToRemove)
            submitData(PagingData.from(currentList))
        }
    }

    companion object {
        private val feedDiffCallback = ItemDiffCallback<Feed>(
            onItemsTheSame = { old, new -> old.feedId == new.feedId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
