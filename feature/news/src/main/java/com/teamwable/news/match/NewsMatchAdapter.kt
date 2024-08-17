package com.teamwable.news.match

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.teamwable.model.NewsMatchModel
import com.teamwable.news.databinding.ItemNewsMatchBinding
import com.teamwable.ui.extensions.ItemDiffCallback

class NewsMatchAdapter : ListAdapter<NewsMatchModel, NewsMatchViewHolder>(
    NewsMatchAdapterDiffCallback,
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsMatchViewHolder {
        val binding = ItemNewsMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsMatchViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsMatchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val NewsMatchAdapterDiffCallback =
            ItemDiffCallback<NewsMatchModel>(
                onItemsTheSame = { old, new -> old.date == new.date },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
