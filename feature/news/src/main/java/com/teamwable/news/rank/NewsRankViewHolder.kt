package com.teamwable.news.rank

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.news.NewsRankModel
import com.teamwable.news.databinding.ItemNewsRankBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.type.LckCupGroup

class NewsRankViewHolder(
    private val binding: ItemNewsRankBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NewsRankModel) {
        with(binding) {
            tvNewsRankRank.text = data.teamRank.toString()
            setTeamProfile(ivNewsRank, data.teamName)
            tvNewsRankTeamName.text = data.teamName
            tvNewsRankWin.text = data.teamWin.toString()
            tvNewsRankLoss.text = data.teamDefeat.toString()
            tvNewsRankWinPercentage.text = "${data.winningRate}%"
            tvNewsRankPointDifference.text = data.scoreDiff.toString()
            if (data.teamName in listOf("T1", "HLE", "DNF", "BRO", "BFX"))
                setRankColor(LckCupGroup.BARON)
            else setRankColor(LckCupGroup.ELDER)
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
            else -> com.teamwable.common.R.drawable.ic_news_match_team_profile
        }
        imageView.setImageResource(resourceId)
    }

    private fun setRankColor(type: LckCupGroup) {
        binding.tvNewsRankRank.setTextColor(itemView.context.colorOf(type.teamColor))
    }
}
