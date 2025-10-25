package com.teamwable.news

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamwable.news.curation.NewsCurationFragment
import com.teamwable.news.match.NewsMatchFragment
import com.teamwable.news.notice.NewsNoticeFragment
import com.teamwable.news.rank.NewsRankFragment

class NewsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = NewsTabType.entries.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            NewsTabType.MATCH.ordinal -> NewsMatchFragment()
            NewsTabType.RANK.ordinal -> NewsRankFragment()
            NewsTabType.CURATION.ordinal -> NewsCurationFragment()
            NewsTabType.NOTICE.ordinal -> NewsNoticeFragment()
            else -> NewsMatchFragment()
        }
    }
}
