package com.teamwable.news.match

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamwable.common.uistate.UiState
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.news.NewsViewModel
import com.teamwable.news.databinding.FragmentNewsMatchBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewsMatchFragment : BindingFragment<FragmentNewsMatchBinding>(FragmentNewsMatchBinding::inflate) {
    private val viewModel: NewsViewModel by viewModels()

    override fun initView() {
        viewModel.getSchedule()

        setupGameTypeObserve()
        setupScheduleObserve()
    }

    private fun setupGameTypeObserve() {
        viewModel.gameTypeUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> setSeasonText(it.data)
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun setSeasonText(gameType: String) {
        binding.viewNewsMatchSeason.tvNewsSeasonTitle.text = gameType
    }

    private fun setupScheduleObserve() {
        viewModel.scheduleUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> initNewsMatchAdapter(it.data)
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initNewsMatchAdapter(scheduleData: List<NewsMatchModel>) = with(binding) {
        if (scheduleData.isEmpty()) {
            llNewsMatchEmpty.visible(true)
            viewNewsMatchSeason.tvNewsSeasonTitle.visible(false)
        } else {
            llNewsMatchEmpty.visible(false)
            viewNewsMatchSeason.tvNewsSeasonTitle.visible(true)

            rvNewsMatchContent.adapter =
                NewsMatchAdapter(requireContext()).apply {
                    submitList(scheduleData)
                }
            rvNewsMatchContent.addItemDecoration(NewsMatchItemDecorator(requireContext()))
        }
    }
}
