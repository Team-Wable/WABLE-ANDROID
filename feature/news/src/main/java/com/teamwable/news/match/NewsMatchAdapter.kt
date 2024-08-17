package com.teamwable.news.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamwable.model.NewsMatchModel
import com.teamwable.news.databinding.ItemNewsMatchBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NewsMatchAdapter() : ListAdapter<NewsMatchModel, NewsMatchViewHolder>(
    NewsMatchAdapterDiffCallback,
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsMatchViewHolder {
        val binding = ItemNewsMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsMatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsMatchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val NewsMatchAdapterDiffCallback =
            ItemDiffCallback<NewsMatchModel>(
                onItemsTheSame = { old, new -> old.aTeamName == new.aTeamName },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
