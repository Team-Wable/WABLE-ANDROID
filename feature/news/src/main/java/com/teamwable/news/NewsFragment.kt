package com.teamwable.news

import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.news.databinding.FragmentNewsBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.stringOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BindingFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {
    override fun initView() {
        initNewsViewPagerAdapter()
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
}
