package com.teamwable.news.match

import android.content.res.ColorStateList
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.news.NewsMatchScoreModel
import com.teamwable.news.R
import com.teamwable.news.databinding.ItemNewsMatchScoreBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.util.CalculateTime

class NewsMatchScoreViewHolder(
    private val binding: ItemNewsMatchScoreBinding,
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
            "BFX" -> com.teamwable.common.R.drawable.ic_news_team_profile_bfx
            "NS" -> com.teamwable.common.R.drawable.ic_news_team_profile_ns
            "DNF" -> com.teamwable.common.R.drawable.ic_news_team_profile_dnf
            "HLE" -> com.teamwable.common.R.drawable.ic_news_team_profile_hle
            "BLG" -> com.teamwable.common.R.drawable.ic_news_team_profile_blg
            "AL" -> com.teamwable.common.R.drawable.ic_news_team_profile_al
            "TES" -> com.teamwable.common.R.drawable.ic_news_team_profile_tes
            "IG" -> com.teamwable.common.R.drawable.ic_news_team_profile_ig
            "FLY" -> com.teamwable.common.R.drawable.ic_news_team_profile_fly
            "VKS" -> com.teamwable.common.R.drawable.ic_news_team_profile_vks
            "100T" -> com.teamwable.common.R.drawable.ic_news_team_profile_100t
            "CFO" -> com.teamwable.common.R.drawable.ic_news_team_profile_cfo
            "TSW" -> com.teamwable.common.R.drawable.ic_news_team_profile_tsw
            "PSG" -> com.teamwable.common.R.drawable.ic_news_team_profile_psg
            "G2" -> com.teamwable.common.R.drawable.ic_news_team_profile_g2
            "MKOI" -> com.teamwable.common.R.drawable.ic_news_team_profile_mkoi
            "FNC" -> com.teamwable.common.R.drawable.ic_news_team_profile_fnc
            else -> com.teamwable.common.R.drawable.ic_news_match_team_profile
        }
        imageView.setImageResource(resourceId)
    }
}
