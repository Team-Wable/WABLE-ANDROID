package com.teamwable.news

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamwable.news.match.NewsMatchFragment
import com.teamwable.news.news.NewsNewsFragment
import com.teamwable.news.notice.NewsNoticeFragment
import com.teamwable.news.rank.NewsRankFragment

class NewsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsMatchFragment()
            1 -> NewsRankFragment()
            2 -> NewsNewsFragment()
            3 -> NewsNoticeFragment()
            else -> NewsMatchFragment()
        }
    }
}
