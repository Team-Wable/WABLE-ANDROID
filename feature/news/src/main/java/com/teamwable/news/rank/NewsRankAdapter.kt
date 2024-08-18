package com.teamwable.news.rank

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamwable.model.NewsRankModel
import com.teamwable.news.databinding.ItemNewsRankBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NewsRankAdapter(context: Context) : ListAdapter<NewsRankModel, NewsRankViewHolder>(
    NewsRankAdapterDiffCallback,
) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRankViewHolder {
        val binding = ItemNewsRankBinding.inflate(inflater, parent, false)
        return NewsRankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsRankViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val NewsRankAdapterDiffCallback =
            ItemDiffCallback<NewsRankModel>(
                onItemsTheSame = { old, new -> old.teamName == new.teamName },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
