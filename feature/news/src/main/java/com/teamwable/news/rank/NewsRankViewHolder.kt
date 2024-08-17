package com.teamwable.news.rank

import androidx.recyclerview.widget.RecyclerView
import com.teamwable.model.NewsRankModel
import com.teamwable.news.databinding.ItemNewsRankBinding

class NewsRankViewHolder(
    private val binding: ItemNewsRankBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NewsRankModel) {
        with(binding) {
            tvNewsRankRank.text = data.teamRank.toString()
            tvNewsRankTeamName.text = data.teamName
            tvNewsRankWin.text = data.teamWin.toString()
            tvNewsRankLoss.text = data.teamDefeat.toString()
            tvNewsRankWinPercentage.text = "${data.winnigRate}%"
            tvNewsRankPointDifference.text = data.scoreDiff.toString()
        }
    }
}
