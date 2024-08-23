package com.teamwable.notification.action

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamwable.common.uistate.UiState
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class NotificationActionFragment : BindingFragment<FragmentNotificationVpBinding>(FragmentNotificationVpBinding::inflate) {
    private val viewModel: NotificationViewModel by viewModels()
    private var notificationAdapter = NotificationActionAdapter(click = { notificationActionData, position -> })

    override fun initView() {
        setupCheckObserve()

        initNotificationActionAdapter()
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

    private fun setupCheckObserve() {
        viewModel.checkUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> Timber.tag("notification").i("patch 성공 : ${it.data}")
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }


    private fun initNotificationActionAdapter() = with(binding) {
        rvNotificationContent.adapter = notificationAdapter

        viewLifeCycleScope.launch {
            viewModel.getNotifications().collectLatest { pagingData ->
                notificationAdapter = NotificationActionAdapter(click = { notificationActionData, position ->
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
                viewModel.patchCheck()
            }

        }
    }
}
