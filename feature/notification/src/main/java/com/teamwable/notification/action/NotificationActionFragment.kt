package com.teamwable.notification.action

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.uistate.UiState
import com.teamwable.notification.NotificationItemDecorator
import com.teamwable.notification.NotificationViewModel
import com.teamwable.notification.databinding.FragmentNotificationVpBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
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
                when (notificationActionData.notificationTriggerType) {
                    requireContext().stringOf(NotificationActionType.CONTENT_LIKED.title) -> toast("contentLiked") // Todo : 게시글 상세 이동
                    requireContext().stringOf(NotificationActionType.COMMENT.title) -> toast("comment") // Todo : 게시글 상세 이동
                    requireContext().stringOf(NotificationActionType.COMMENT_LIKED.title) -> toast("commentLiked") // Todo : 게시글 상세 이동
                    requireContext().stringOf(NotificationActionType.ACTING_CONTINUE.title) -> toast("actingContinue") // Todo : 글쓰기 이동
                    requireContext().stringOf(NotificationActionType.BE_GHOST.title) -> toast("beGhost") // Todo : 게시글 상세 이동
                    requireContext().stringOf(NotificationActionType.CONTENT_GHOST.title) -> toast("contentGhost") // Todo : 게시글 상세 이동
                    requireContext().stringOf(NotificationActionType.COMMENT_GHOST.title) -> toast("commentGhost") // Todo : 게시글 상세 이동
                    requireContext().stringOf(NotificationActionType.USER_BAN.title) -> toast("userBan") // Todo : Unit
                    requireContext().stringOf(NotificationActionType.POPULAR_WRITER.title) -> toast("popularWriter") // Todo : 게시글 상세 이동
                    requireContext().stringOf(NotificationActionType.POPULAR_CONTENT.title) -> toast("popularContent") // Todo : 게시글 상세 이동
                }
            },
            onProfileClick = {
                toast("profile") // Todo : 마페뷰 이동
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
            viewModel.getNotifications().collectLatest { pagingData ->
                notificationAdapter.submitData(pagingData)
            }
            viewModel.patchCheck()
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
