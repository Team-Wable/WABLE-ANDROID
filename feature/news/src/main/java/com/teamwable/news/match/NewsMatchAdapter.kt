package com.teamwable.news.match

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.news.databinding.ItemNewsMatchBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NewsMatchAdapter(context: Context) : ListAdapter<NewsMatchModel, NewsMatchViewHolder>(
    NewsMatchAdapterDiffCallback,
) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsMatchViewHolder {
        val binding = ItemNewsMatchBinding.inflate(inflater, parent, false)
        return NewsMatchViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsMatchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val NewsMatchAdapterDiffCallback =
            ItemDiffCallback<NewsMatchModel>(
                onItemsTheSame = { old, new -> old.games == new.games },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
