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
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationInformationFragment : BindingFragment<FragmentNotificationVpBinding>(FragmentNotificationVpBinding::inflate) {
    private val viewModel: NotificationViewModel by viewModels()
    private var notificationAdapter = NotificationInformationAdapter(click = { notificationInformationData, position -> })

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

            // Todo : 데이터 새로고침 추가해야 함
            swipeNotificationVp.isRefreshing = false
        }
    }

    private fun initNotificationInformationAdapter() = with(binding) {
        rvNotificationContent.adapter = notificationAdapter

        viewLifeCycleScope.launch {
            viewModel.getInformation().collectLatest { pagingData ->
                NotificationInformationAdapter(click = { notificationInformationData, position ->
                    when (notificationInformationData.infoNotificationType) {
                        "GAMEDONE" -> toast("GAMEDONE")
                        "GAMESTART" -> toast("GAMESTART")
                        "WEEKDONE" -> toast("WEEKDONE")
                    }
                }).apply {
                    notificationAdapter.submitData(pagingData)
                }

                if (rvNotificationContent.itemDecorationCount == 0) {
                    rvNotificationContent.addItemDecoration(NotificationItemDecorator(requireContext()))
                }

                notificationAdapter.addLoadStateListener { combinedLoadStates ->
                    if (combinedLoadStates.append.endOfPaginationReached) {
                        if (notificationAdapter.itemCount < 1) {
                            llNotificationVpEmpty.visible(true)
                        } else {
                            llNotificationVpEmpty.visible(false)
                        }
                    }
                }
            }
        }
    }
}
