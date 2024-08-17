package com.teamwable.notification

import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.notification.databinding.FragmentNotificationBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.stringOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    override fun initView() {
        initNotificationViewPagerAdapter()
    }

    private fun initNotificationViewPagerAdapter() {
        with(binding) {
            vpNotification.adapter = NotificationViewPagerAdapter(this@NotificationFragment)
            TabLayoutMediator(tlNotification, vpNotification) { tab, position ->
                when (position) {
                    0 -> tab.text = stringOf(R.string.tv_notification_tab_action)
                    1 -> tab.text = stringOf(R.string.tv_notification_tab_information)
                }
            }.attach()
        }
    }
}
