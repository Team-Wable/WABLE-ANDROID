package com.teamwable.news.rank

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.uistate.UiState
import com.teamwable.model.news.NewsRankModel
import com.teamwable.news.NewsViewModel
import com.teamwable.news.databinding.FragmentNewsRankBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.openUri
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewsRankFragment : BindingFragment<FragmentNewsRankBinding>(FragmentNewsRankBinding::inflate) {
    private val viewModel: NewsViewModel by viewModels()

    override fun initView() {
        viewModel.getRank()
        setOpinionText()

        setupGameTypeObserve()
        setupRankObserve()

        initOpinionBtnClickListener()
    }

    private fun setupGameTypeObserve() {
        viewModel.gameTypeUiState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> setSeasonText(it.data)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setSeasonText(gameType: String) {
        binding.viewNewsRankSeason.tvNewsSeasonTitle.text = gameType
    }

    private fun initOpinionBtnClickListener() {
        binding.btnNewsRankOpinion.setOnClickListener {
            openUri("https://forms.gle/WWfbHXvGNgXMxgZr5")
        }
    }

    private fun setupRankObserve() {
        viewModel.rankUiState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> initNewsRankAdapter(it.data)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initNewsRankAdapter(rankData: List<NewsRankModel>) = with(binding) {
        rvNewsRank.adapter =
            NewsRankAdapter(requireContext()).apply {
                submitList(rankData)
            }
        rvNewsRank.addItemDecoration(NewsRankItemDecorator(requireContext()))
    }

    private fun setOpinionText() {
        val spannableString = SpannableString(binding.btnNewsRankOpinion.text)

        spannableString.setSpan(
            ForegroundColorSpan(colorOf(com.teamwable.ui.R.color.sky_50)),
            16, spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.btnNewsRankOpinion.text = spannableString
    }
}
