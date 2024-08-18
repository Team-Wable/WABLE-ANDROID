package com.teamwable.news.match

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.NewsMatchModel
import com.teamwable.news.databinding.ItemNewsMatchBinding
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.util.CalculateTime

class NewsMatchViewHolder(
    private val binding: ItemNewsMatchBinding
) : RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: NewsMatchModel) {
        with(binding) {
            tvNewsMatchToday.visible(CalculateTime().isToday(data.date))
            tvNewsMatchDate.text = CalculateTime().formatDate(data.date)

            rvNewsMatchScore.adapter = NewsMatchScoreAdapter(root.context).apply { submitList(data.games) }
            rvNewsMatchScore.addItemDecoration(NewsMatchScoreItemDecorator(binding.root.context))
        }
    }
}
