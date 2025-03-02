package com.teamwable.viewit

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.extensions.ItemDiffCallback

class ViewItAdapter(private val viewItClickListener: ViewItClickListener) : PagingDataAdapter<ViewIt, ViewItViewHolder>(viewItDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewItViewHolder = ViewItViewHolder.from(parent, viewItClickListener)

    override fun onBindViewHolder(holder: ViewItViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val viewItDiffCallback = ItemDiffCallback<ViewIt>(
            onItemsTheSame = { old, new -> old.viewItId == new.viewItId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
