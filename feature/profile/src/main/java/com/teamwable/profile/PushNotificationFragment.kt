package com.teamwable.profile

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.FragmentPushNotificationBinding
import com.teamwable.ui.base.BindingFragment

class PushNotificationFragment : BindingFragment<FragmentPushNotificationBinding>(FragmentPushNotificationBinding::inflate) {
    override fun initView() {
        setAppbarText()
        initBackBtnClickListener()
    }

    private fun setAppbarText() {
        binding.viewPushNotificationAppbar.tvProfileAppbarTitle.text = "알림 설정"
    }

    private fun initBackBtnClickListener() {
        binding.viewPushNotificationAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
