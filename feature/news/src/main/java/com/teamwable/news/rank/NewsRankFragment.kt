package com.teamwable.news.rank

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.viewModels
import com.teamwable.news.NewsViewModel
import com.teamwable.news.databinding.FragmentNewsRankBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsRankFragment : BindingFragment<FragmentNewsRankBinding>(FragmentNewsRankBinding::inflate) {
    private val viewModel: NewsViewModel by viewModels()

    override fun initView() {
        setSeasonText()
        setOpinionText()

        initNewsRankAdapter()

        initOpinionBtnClickListener()
    }

    private fun setSeasonText() {
        binding.viewNewsRankSeason.tvNewsSeasonTitle.text = "2024 Wable LCK"
    }

    private fun initOpinionBtnClickListener() {
        binding.btnNewsRankOpinion.setOnClickListener {
            // Todo : 나중에 추가해야 함
        }
    }

    private fun initNewsRankAdapter() {
        binding.rvNewsRank.adapter =
            NewsRankAdapter().apply {
                submitList(viewModel.mockNewsRankList)
            }
        binding.rvNewsRank.addItemDecoration(NewsRankItemDecorator(requireContext()))
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
