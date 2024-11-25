package com.teamwable.news

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.common.uistate.UiState
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_GAMESCHEDULE
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_NEWS
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_NOTICE
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_RANKING
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.news.databinding.FragmentNewsBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.statusBarColorOf
import com.teamwable.ui.extensions.statusBarModeOf
import com.teamwable.ui.extensions.stringOf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class NewsFragment : BindingFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {
    private val viewModel: NewsViewModel by viewModels()

    override fun initView() {
        statusBarColorOf(com.teamwable.ui.R.color.black)
        statusBarModeOf(false)

        initNewsViewPagerAdapter()
        initTabClickListener()

        setupNumberObserve()
    }

    private fun setupNumberObserve() {
        viewModel.newsNumberUiState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    saveNumberFromServerToLocal(
                        state.data["news"]?.takeIf { it >= 0 } ?: 0,
                        state.data["notice"]?.takeIf { it >= 0 } ?: 0
                    )
                }

                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private suspend fun saveNumberFromServerToLocal(serverNewsNumber: Int, serverNoticeNumber: Int) {
//        viewModel.saveNewsNumber(1)
//        viewModel.saveNoticeNumber(2)

        val localNewsNumber = viewModel.getNewsNumberFromLocal()
        val localNoticeNumber = viewModel.getNoticeNumberFromLocal()

        if (serverNewsNumber > localNewsNumber) {
            Timber.tag("here").d("news server: $serverNewsNumber, local: $localNewsNumber")
            setBadgeOnNews(NewsTabType.NEWS.idx, true)
            viewModel.saveNewsNumber(serverNewsNumber)
        } else {
            Timber.tag("here").d("equal news server: $serverNewsNumber, local: $localNewsNumber")
        }

        if (serverNoticeNumber > localNoticeNumber) {
            Timber.tag("here").d("notice server: $serverNoticeNumber, local: $localNoticeNumber")
            setBadgeOnNews(NewsTabType.NOTICE.idx, true)
            viewModel.saveNoticeNumber(serverNoticeNumber)
        } else {
            Timber.tag("here").d("equal notice server: $serverNoticeNumber, local: $localNoticeNumber")
        }
    }

    private fun setBadgeOnNews(idx: Int, isVisible: Boolean) {
        binding.tlNews.getTabAt(idx)?.getOrCreateBadge()?.apply {
            this.isVisible = isVisible
            horizontalOffset = 1
            if (isVisible) backgroundColor = colorOf(com.teamwable.ui.R.color.error) else clearNumber()
        }
    }

    private fun initNewsViewPagerAdapter() {
        with(binding) {
            vpNews.adapter = NewsViewPagerAdapter(this@NewsFragment)
            TabLayoutMediator(tlNews, vpNews) { tab, position ->
                when (position) {
                    NewsTabType.MATCH.idx -> tab.text = stringOf(R.string.tv_news_tab_match)
                    NewsTabType.RANK.idx -> tab.text = stringOf(R.string.tv_news_tab_rank)
                    NewsTabType.NEWS.idx -> tab.text = stringOf(R.string.tv_news_tab_news)
                    NewsTabType.NOTICE.idx -> tab.text = stringOf(R.string.tv_news_tab_notice)
                }
            }.attach()
        }
    }

    private fun initTabClickListener() {
        binding.tlNews.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    NewsTabType.MATCH.idx -> trackEvent(CLICK_GAMESCHEDULE)
                    NewsTabType.RANK.idx -> trackEvent(CLICK_RANKING)
                    NewsTabType.NEWS.idx -> {
                        trackEvent(CLICK_NEWS)
                        setBadgeOnNews(NewsTabType.NEWS.idx, false)
                    }

                    NewsTabType.NOTICE.idx -> {
                        trackEvent(CLICK_NOTICE)
                        setBadgeOnNews(NewsTabType.NOTICE.idx, false)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        statusBarColorOf(com.teamwable.ui.R.color.white)
        statusBarModeOf()
    }
}
