package com.teamwable.notification.information

import androidx.fragment.app.viewModels
import com.teamwable.notification.NotificationItemDecorator
import com.teamwable.notification.NotificationViewModel
import com.teamwable.notification.databinding.FragmentNotificationVpBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationInformationFragment : BindingFragment<FragmentNotificationVpBinding>(FragmentNotificationVpBinding::inflate) {
    private val viewModel: NotificationViewModel by viewModels()

    override fun initView() {
        initNotificationInformationAdapter()
    }

    private fun initNotificationInformationAdapter() {
        if (viewModel.mockNotificationInformationList.isEmpty()) {
            binding.llNotificationVpEmpty.visible(true)
        } else {
            binding.llNotificationVpEmpty.visible(false)

            binding.rvNotificationContent.adapter =
                NotificationInformationAdapter(requireContext(), click = { notificationInformationData, position ->
                    when (notificationInformationData.infoNotificationType) {
                        "GAMEDONE" -> toast("GAMEDONE")
                        "GAMESTART" -> toast("GAMESTART")
                        "WEEKDONE" -> toast("WEEKDONE")
                    }
                }).apply {
                    submitList(viewModel.mockNotificationInformationList)
                }
            binding.rvNotificationContent.addItemDecoration(NotificationItemDecorator(requireContext()))
        }
    }
}
