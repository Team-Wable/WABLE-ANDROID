package com.teamwable.news.rank

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.NewsRankModel
import com.teamwable.news.databinding.ItemNewsRankBinding

class NewsRankViewHolder(
    private val binding: ItemNewsRankBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NewsRankModel) {
        with(binding) {
            tvNewsRankRank.text = data.teamRank.toString()
            setTeamProfile(ivNewsRank, data.teamName)
            tvNewsRankTeamName.text = data.teamName
            tvNewsRankWin.text = data.teamWin.toString()
            tvNewsRankLoss.text = data.teamDefeat.toString()
            tvNewsRankWinPercentage.text = "${data.winnigRate}%"
            tvNewsRankPointDifference.text = data.scoreDiff.toString()
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
