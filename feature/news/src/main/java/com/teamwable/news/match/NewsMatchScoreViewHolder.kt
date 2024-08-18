package com.teamwable.news.match

import android.content.res.ColorStateList
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.NewsMatchScoreModel
import com.teamwable.news.R
import com.teamwable.news.databinding.ItemNewsMatchScoreBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.util.CalculateTime

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

            tvNewsMatchScoreTime.text = CalculateTime().formatTime(data.gameDate)

            setTeamProfile(ivNewsMatchScoreFirstSymbol, data.aTeamName)
            tvNewsMatchScoreFirstName.text = data.aTeamName
            tvNewsMatchScoreFirstScore.text = data.aTeamScore.toString()
            setTeamProfile(ivNewsMatchScoreSecondSymbol, data.bTeamName)
            tvNewsMatchScoreSecondName.text = data.bTeamName
            tvNewsMatchScoreSecondScore.text = data.bTeamScore.toString()
        }
    }

    private fun setTeamProfile(imageView: ImageView, teamName: String) {
        val resourceId = when (teamName) {
            "T1" -> com.teamwable.common.R.drawable.ic_news_team_profile_t1
            "GEN" -> com.teamwable.common.R.drawable.ic_news_team_profile_gen
            "BRO" -> com.teamwable.common.R.drawable.ic_news_team_profile_bro
            "DRX" -> com.teamwable.common.R.drawable.ic_news_team_profile_drx
            "DK" -> com.teamwable.common.R.drawable.ic_news_team_profile_dk
            "KT" -> com.teamwable.common.R.drawable.ic_news_team_profile_kt
            "FOX" -> com.teamwable.common.R.drawable.ic_news_team_profile_fox
            "NS" -> com.teamwable.common.R.drawable.ic_news_team_profile_ns
            "KDF" -> com.teamwable.common.R.drawable.ic_news_team_profile_kdf
            "HLE" -> com.teamwable.common.R.drawable.ic_news_team_profile_hle
            else -> com.teamwable.common.R.drawable.ic_news_match_team_profile
        }
        imageView.setImageResource(resourceId)
    }
}
