package com.teamwable.news

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamwable.news.match.NewsMatchFragment
import com.teamwable.news.news.NewsNewsFragment
import com.teamwable.news.notice.NewsNoticeFragment
import com.teamwable.news.rank.NewsRankFragment

class NewsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NewsTabType.entries.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            NewsTabType.MATCH.idx -> NewsMatchFragment()
            NewsTabType.RANK.idx -> NewsRankFragment()
            NewsTabType.NEWS.idx -> NewsNewsFragment()
            NewsTabType.NOTICE.idx -> NewsNoticeFragment()
            else -> NewsMatchFragment()
        }
    }
}
