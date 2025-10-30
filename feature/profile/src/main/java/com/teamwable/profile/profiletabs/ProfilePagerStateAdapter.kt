package com.teamwable.profile.profiletabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamwable.ui.type.ProfileUserType

class ProfilePagerStateAdapter(
    fragment: Fragment,
    private val userID: Long,
    private val nickName: String,
    private val type: ProfileUserType,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = ProfileTabType.entries.size

    override fun createFragment(position: Int): Fragment =
        when (ProfileTabType.entries[position]) {
            ProfileTabType.FEED -> ProfileFeedListFragment.newInstance(userID, nickName, type)
            ProfileTabType.COMMENT -> ProfileCommentListFragment.newInstance(userID, nickName, type)
        }
}
