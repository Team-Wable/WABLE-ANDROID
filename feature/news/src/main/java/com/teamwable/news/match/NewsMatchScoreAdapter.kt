package com.teamwable.news.match

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.teamwable.model.news.NewsMatchScoreModel
import com.teamwable.news.databinding.ItemNewsMatchScoreBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NewsMatchScoreAdapter(context: Context) : ListAdapter<NewsMatchScoreModel, NewsMatchScoreViewHolder>(
    NewsMatchScoreAdapterDiffCallback,
) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsMatchScoreViewHolder {
        val binding = ItemNewsMatchScoreBinding.inflate(inflater, parent, false)
        return NewsMatchScoreViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsMatchScoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val NewsMatchScoreAdapterDiffCallback =
            ItemDiffCallback<NewsMatchScoreModel>(
                onItemsTheSame = { old, new -> old.gameDate == new.gameDate },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
