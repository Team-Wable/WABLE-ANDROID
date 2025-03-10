package com.teamwable.notification

import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.common.util.AmplitudeNotiTag.CLICK_ACTIVITIESNOTI
import com.teamwable.common.util.AmplitudeNotiTag.CLICK_INFONOTI
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.notification.databinding.FragmentNotificationBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.stringOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    override fun initView() {
        initNotificationViewPagerAdapter()
        initTabClickListener()
        initUpBtnClickListener()
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

    private fun initTabClickListener() {
        binding.tlNotification.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> trackEvent(CLICK_ACTIVITIESNOTI)
                    1 -> trackEvent(CLICK_INFONOTI)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun initUpBtnClickListener() {
        binding.tbNotification.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
