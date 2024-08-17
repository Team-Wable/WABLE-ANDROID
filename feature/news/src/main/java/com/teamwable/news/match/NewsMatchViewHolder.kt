package com.teamwable.news.match

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.NewsMatchModel
import com.teamwable.news.databinding.ItemNewsMatchBinding
import com.teamwable.ui.extensions.visible
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class NewsMatchViewHolder(
    private val binding: ItemNewsMatchBinding
) : RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: NewsMatchModel) {
        with(binding) {
            tvNewsMatchToday.visible(isToday(data.date))
            tvNewsMatchDate.text = formatDate(data.date)

            rvNewsMatchScore.adapter = NewsMatchScoreAdapter().apply { submitList(data.games) }
            rvNewsMatchScore.addItemDecoration(NewsMatchScoreItemDecorator(binding.root.context))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isToday(inputDate: String): Boolean {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(inputDate, dateFormatter).isEqual(LocalDate.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("MM. dd (E)")

        val date = LocalDate.parse(inputDate, inputFormatter)
        val koreanDayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ko", "KR"))

        return date.format(outputFormatter).replace(date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), koreanDayOfWeek)
    }
}
