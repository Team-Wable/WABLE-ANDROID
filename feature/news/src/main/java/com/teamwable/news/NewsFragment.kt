package com.teamwable.news

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_GAMESCHEDULE
import com.teamwable.common.util.AmplitudeNewsTag.CLICK_RANKING
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.news.databinding.FragmentNewsBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.stringOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BindingFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {
    override fun initView() {
        initNewsViewPagerAdapter()
        initTabClickListener()
    }

    private fun initNewsViewPagerAdapter() {
        with(binding) {
            vpNews.adapter = NewsViewPagerAdapter(this@NewsFragment)
            TabLayoutMediator(tlNews, vpNews) { tab, position ->
                when (position) {
                    0 -> tab.text = stringOf(R.string.tv_news_tab_match)
                    1 -> tab.text = stringOf(R.string.tv_news_tab_rank)
                }
            }.attach()
        }
    }

    private fun initTabClickListener() {
        binding.tlNews.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> trackEvent(CLICK_GAMESCHEDULE)
                    1 -> trackEvent(CLICK_RANKING)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}
