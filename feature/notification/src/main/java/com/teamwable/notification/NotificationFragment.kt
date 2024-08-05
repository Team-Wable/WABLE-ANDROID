package com.teamwable.notification

import com.teamwable.notification.databinding.FragmentNotificationBinding
import com.teamwable.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    override fun initView() {
    }
}
