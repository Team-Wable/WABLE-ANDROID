package com.teamwable.news

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_GAMESCHEDULE
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_NEWS
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_NOTICE
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_RANKING
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.news.databinding.FragmentNewsBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BindingFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {
    override fun initView() {
        initNewsViewPagerAdapter()
        initTabClickListener()

        setBadgeOnNews(NewsTabType.NEWS.idx, true)
        setBadgeOnNews(NewsTabType.NOTICE.idx, true)
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
}
