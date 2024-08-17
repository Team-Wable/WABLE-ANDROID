package com.teamwable.news.match

import androidx.fragment.app.viewModels
import com.teamwable.news.NewsViewModel
import com.teamwable.news.databinding.FragmentNewsMatchBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsMatchFragment : BindingFragment<FragmentNewsMatchBinding>(FragmentNewsMatchBinding::inflate) {
    private val viewModel: NewsViewModel by viewModels()

    override fun initView() {
        setSeasonText()
    }

    private fun setSeasonText() {
        binding.viewNewsMatchSeason.tvNewsSeasonTitle.text = "2024 Wable LCK"
    }

    private fun initAdapter() {
        if (viewModel.mockNewsMatchList.isEmpty()) {
            binding.llNewsMatchEmpty.visible(true)
            binding.viewNewsMatchSeason.tvNewsSeasonTitle.visible(false)
        } else {
            binding.llNewsMatchEmpty.visible(false)
            binding.viewNewsMatchSeason.tvNewsSeasonTitle.visible(true)
        }
    }
}
