package com.teamwable.notification

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamwable.notification.action.NotificationActionFragment
import com.teamwable.notification.information.NotificationInformationFragment

class NotificationViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NotificationActionFragment()
            1 -> NotificationInformationFragment()
            else -> NotificationActionFragment()
        }
    }
}
