package com.teamwable.notification.action

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.teamwable.common.uistate.UiState
import com.teamwable.notification.NotificationItemDecorator
import com.teamwable.notification.NotificationViewModel
import com.teamwable.notification.databinding.FragmentNotificationVpBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.PagingLoadingAdapter
import com.teamwable.ui.util.Arg.FEED_ID
import com.teamwable.ui.util.Arg.PROFILE_USER_ID
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class NotificationActionFragment : BindingFragment<FragmentNotificationVpBinding>(FragmentNotificationVpBinding::inflate) {
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationActionAdapter

    override fun initView() {
        initNotificationActionAdapter()

        setupCheckObserve()
    }

    private fun setupCheckObserve() {
        viewModel.checkUiState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> Timber.tag("notification").i("patch 성공 : ${it.data}")
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initNotificationActionAdapter() = with(binding) {
        notificationAdapter = NotificationActionAdapter(
            onNotificationClick = { notificationActionData, position ->
                if (notificationActionData.notificationTriggerType == stringOf(NotificationActionType.VIEW_IT_LIKED.title))
                    (activity as Navigation).navigateToViewItFragment()
                else
                    findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.HomeDetail, mapOf(FEED_ID to notificationActionData.notificationTriggerId))
            },
            onProfileClick = { userId ->
                if (userId != -1) findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Profile, mapOf(PROFILE_USER_ID to userId))
            },
        )

        rvNotificationContent.adapter = notificationAdapter.withLoadStateFooter(PagingLoadingAdapter())
        if (rvNotificationContent.itemDecorationCount == 0) {
            rvNotificationContent.addItemDecoration(NotificationItemDecorator(requireContext()))
        }

        submitList()
        setEmptyLayout()
        setSwipeLayout()
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            viewModel.getNotifications().collectLatest { pagingData ->
                notificationAdapter.submitData(pagingData)
            }
        }
        viewModel.patchCheck()
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
