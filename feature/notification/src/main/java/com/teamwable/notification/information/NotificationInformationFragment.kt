package com.teamwable.notification.information

import androidx.fragment.app.viewModels
import com.teamwable.notification.NotificationItemDecorator
import com.teamwable.notification.NotificationViewModel
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
    private lateinit var notificationAdapter: NotificationInformationAdapter

    override fun initView() {
        initNotificationInformationAdapter()
    }

    private fun initNotificationInformationAdapter() = with(binding) {
        notificationAdapter = NotificationInformationAdapter(click = { notificationInformationData, position ->
            when (notificationInformationData.infoNotificationType) {
                "GAMEDONE" -> toast("GAMEDONE") // Todo : 소식 경기 탭으로 이동
                "GAMESTART" -> toast("GAMESTART") // Todo : Unit
                "WEEKDONE" -> toast("WEEKDONE") // Todo : 소식 경기 탭으로 이동
            }
        })

        rvNotificationContent.adapter = notificationAdapter
        if (rvNotificationContent.itemDecorationCount == 0) {
            rvNotificationContent.addItemDecoration(NotificationItemDecorator(requireContext()))
        }

        submitList()
        setEmptyLayout()
        setSwipeLayout()
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            viewModel.getInformation().collectLatest { pagingData ->
                notificationAdapter.submitData(pagingData)
            }
        }
    }

    private fun setEmptyLayout() = with(binding) {
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

    private fun setSwipeLayout() = with(binding) {
        swipeNotificationVp.setOnRefreshListener {
            notificationAdapter.refresh()
            swipeNotificationVp.isRefreshing = false
        }
    }
}
