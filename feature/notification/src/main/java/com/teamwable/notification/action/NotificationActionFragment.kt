package com.teamwable.notification.action

import androidx.fragment.app.viewModels
import com.teamwable.notification.NotificationItemDecorator
import com.teamwable.notification.NotificationViewModel
import com.teamwable.notification.databinding.FragmentNotificationVpBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActionFragment : BindingFragment<FragmentNotificationVpBinding>(FragmentNotificationVpBinding::inflate) {
    private val viewModel: NotificationViewModel by viewModels()

    override fun initView() {
        initNotificationActionAdapter()
    }

    private fun initNotificationActionAdapter() {
        if (viewModel.mockNotificationActionList.isEmpty()) {
            binding.llNotificationVpEmpty.visible(true)
        } else {
            binding.llNotificationVpEmpty.visible(false)

            binding.rvNotificationContent.adapter =
                NotificationActionAdapter(requireContext(), click = { notificationActionData, position ->
                    when (notificationActionData.notificationTriggerType) {
                        "contentLiked" -> toast("contentLiked")
                        "comment" -> toast("comment")
                        "commentLiked" -> toast("commentLiked")
                        "actingContinue" -> toast("actingContinue")
                        "beGhost" -> toast("beGhost")
                        "contentGhost" -> toast("contentGhost")
                        "commentGhost" -> toast("commentGhost")
                        "userBan" -> toast("userBan")
                        "popularWriter" -> toast("popularWriter")
                    }
                }).apply {
                    submitList(viewModel.mockNotificationActionList)
                }
            binding.rvNotificationContent.addItemDecoration(NotificationItemDecorator(requireContext()))
        }
    }
}
