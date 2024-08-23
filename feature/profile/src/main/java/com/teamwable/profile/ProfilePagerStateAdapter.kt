package com.teamwable.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePagerStateAdapter(
    fragment: Fragment,
    private val userID: Long,
    private val nickName: String,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = ProfileTabType.entries.size

    override fun createFragment(position: Int): Fragment =
        when (ProfileTabType.entries[position]) {
            ProfileTabType.FEED -> ProfileFeedListFragment.newInstance(userID, nickName)
            ProfileTabType.COMMENT -> ProfileCommentListFragment.newInstance(userID, nickName)
        }
}
