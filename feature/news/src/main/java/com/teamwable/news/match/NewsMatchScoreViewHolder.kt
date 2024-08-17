package com.teamwable.news.match

import android.content.res.ColorStateList
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.NewsMatchScoreModel
import com.teamwable.news.R
import com.teamwable.news.databinding.ItemNewsMatchScoreBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewsMatchScoreViewHolder(
    private val binding: ItemNewsMatchScoreBinding
) : RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: NewsMatchScoreModel) {
        with(binding) {
            tvNewsMatchScoreState.apply {
                when (data.gameStatus) {
                    "SCHEDULED" -> {
                        text = context.stringOf(R.string.tv_news_match_score_state_planned)
                        backgroundTintList = ColorStateList.valueOf(context.colorOf(com.teamwable.ui.R.color.warning))
                    }

                    "PROGRESS" -> {
                        text = context.stringOf(R.string.tv_news_match_score_state_ongoing)
                        backgroundTintList = ColorStateList.valueOf(context.colorOf(com.teamwable.ui.R.color.info))
                    }

                    "TERMINATION" -> {
                        text = context.stringOf(R.string.tv_news_match_score_state_finished)
                        backgroundTintList = ColorStateList.valueOf(context.colorOf(com.teamwable.ui.R.color.error))
                    }
                }
            }

            tvNewsMatchScoreTime.text = formatDateTime(data.gameDate)
            tvNewsMatchScoreFirstName.text = data.aTeamName
            tvNewsMatchScoreFirstScore.text = data.aTeamScore.toString()
            tvNewsMatchScoreSecondName.text = data.bTeamName
            tvNewsMatchScoreSecondScore.text = data.bTeamScore.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateTime(inputDateTime: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")

        return LocalDateTime.parse(inputDateTime, inputFormatter).format(outputFormatter)
    }
}
