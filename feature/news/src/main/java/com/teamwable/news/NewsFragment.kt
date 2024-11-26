package com.teamwable.news

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
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
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class NewsFragment : BindingFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {
    private val viewModel: NewsViewModel by viewModels()

    private var serverNewsNumber = -1
    private var serverNoticeNumber = -1

    override fun initView() {
        statusBarColorOf(com.teamwable.ui.R.color.black)
        statusBarModeOf(false)

        initNewsViewPagerAdapter()
        initTabClickListener()

        setupNumberObserve()
    }

    private fun setupNumberObserve() {
        viewModel.newsNumberUiState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    serverNewsNumber = getServerNumber(state, "news")
                    serverNoticeNumber = getServerNumber(state, "notice")

                    saveNumberFromServerToLocal()
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun getServerNumber(state: UiState.Success<Map<String, Int>>, idx: String) =
        state.data[idx]?.takeIf { it >= 0 } ?: 0


    private suspend fun saveNumberFromServerToLocal() {
//        viewModel.saveNewsNumber(1)
//        viewModel.saveNoticeNumber(2)

        val localNewsNumber = viewModel.getNewsNumberFromLocal()
        val localNoticeNumber = viewModel.getNoticeNumberFromLocal()

        if (serverNewsNumber > localNewsNumber) {
            Timber.tag("here").d("news server: $serverNewsNumber, local: $localNewsNumber")
            setBadgeOnNews(NewsTabType.NEWS.ordinal, true)
        } else {
            Timber.tag("here").d("equal news server: $serverNewsNumber, local: $localNewsNumber")
        }

        if (serverNoticeNumber > localNoticeNumber) {
            Timber.tag("here").d("notice server: $serverNoticeNumber, local: $localNoticeNumber")
            setBadgeOnNews(NewsTabType.NOTICE.ordinal, true)
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
                    NewsTabType.MATCH.ordinal -> tab.text = stringOf(R.string.tv_news_tab_match)
                    NewsTabType.RANK.ordinal -> tab.text = stringOf(R.string.tv_news_tab_rank)
                    NewsTabType.NEWS.ordinal -> tab.text = stringOf(R.string.tv_news_tab_news)
                    NewsTabType.NOTICE.ordinal -> tab.text = stringOf(R.string.tv_news_tab_notice)
                }
            }.attach()
        }
    }

    private fun initTabClickListener() {
        binding.tlNews.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    NewsTabType.MATCH.ordinal -> trackEvent(CLICK_GAMESCHEDULE)
                    NewsTabType.RANK.ordinal -> trackEvent(CLICK_RANKING)
                    NewsTabType.NEWS.ordinal -> {
                        trackEvent(CLICK_NEWS)
                        setBadgeOnNews(NewsTabType.NEWS.ordinal, false)
                        viewModel.saveNewsNumber(serverNewsNumber)
                    }

                    NewsTabType.NOTICE.ordinal -> {
                        trackEvent(CLICK_NOTICE)
                        setBadgeOnNews(NewsTabType.NOTICE.ordinal, false)
                        viewModel.saveNoticeNumber(serverNoticeNumber)
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
