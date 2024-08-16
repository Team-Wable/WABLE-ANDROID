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
        binding.viewPushNotificationAppbar.tvProfileAppbarTitle.text = getString(R.string.appbar_push_notification_title)
    }

    private fun initBackBtnClickListener() {
        binding.viewPushNotificationAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
