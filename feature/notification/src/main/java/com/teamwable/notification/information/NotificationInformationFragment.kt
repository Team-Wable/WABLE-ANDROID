package com.teamwable.notification.information

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import com.teamwable.notification.NotificationItemDecorator
import com.teamwable.notification.NotificationViewModel
import com.teamwable.notification.R
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

        initSwipeRefreshData()
    }

    private fun initSwipeRefreshData() = with(binding) {
        swipeNotificationVp.setOnRefreshListener {
            val slideDown = AnimationUtils.loadAnimation(context, R.anim.anim_swipe_refresh_slide_down)
            val slideUp = AnimationUtils.loadAnimation(context, R.anim.anim_swipe_refresh_slide_up)

            slideDown.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        rvNotificationContent.startAnimation(slideUp)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                },
            )
            rvNotificationContent.startAnimation(slideDown)

            swipeNotificationVp.isRefreshing = false
        }
    }

    private fun initNotificationInformationAdapter() = with(binding) {
        if (viewModel.mockNotificationInformationList.isEmpty()) {
            llNotificationVpEmpty.visible(true)
        } else {
            llNotificationVpEmpty.visible(false)

            rvNotificationContent.adapter =
                NotificationInformationAdapter(requireContext(), click = { notificationInformationData, position ->
                    when (notificationInformationData.infoNotificationType) {
                        "GAMEDONE" -> toast("GAMEDONE")
                        "GAMESTART" -> toast("GAMESTART")
                        "WEEKDONE" -> toast("WEEKDONE")
                    }
                }).apply {
                    submitList(viewModel.mockNotificationInformationList)
                }
            if (rvNotificationContent.itemDecorationCount == 0) rvNotificationContent.addItemDecoration(NotificationItemDecorator(requireContext()))
        }
    }
}
