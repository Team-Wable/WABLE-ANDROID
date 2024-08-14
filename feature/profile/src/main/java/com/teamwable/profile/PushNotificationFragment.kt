package com.teamwable.profile

import com.teamwable.profile.databinding.FragmentPushNotificationBinding
import com.teamwable.ui.base.BindingFragment

class PushNotificationFragment : BindingFragment<FragmentPushNotificationBinding>(FragmentPushNotificationBinding::inflate) {
    override fun initView() {
        setAppbarText()
    }

    private fun setAppbarText() {
        binding.viewPushNotificationAppbar.tvProfileAppbarTitle.text = "알림 설정"
    }
}
