package com.teamwable.news.rank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamwable.model.NewsRankModel
import com.teamwable.news.databinding.ItemNewsRankBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NewsRankAdapter : ListAdapter<NewsRankModel, NewsRankViewHolder>(
    NewsRankAdapterDiffCallback,
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRankViewHolder {
        val binding = ItemNewsRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsRankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsRankViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val NewsRankAdapterDiffCallback =
            ItemDiffCallback<NewsRankModel>(
                onItemsTheSame = { old, new -> old.teamName == new.teamName },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
