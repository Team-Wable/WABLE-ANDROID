package com.teamwable.notification.action

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.uistate.UiState
import com.teamwable.notification.NotificationItemDecorator
import com.teamwable.notification.NotificationViewModel
import com.teamwable.notification.databinding.FragmentNotificationVpBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class NotificationActionFragment : BindingFragment<FragmentNotificationVpBinding>(FragmentNotificationVpBinding::inflate) {
    private val viewModel: NotificationViewModel by viewModels()

    override fun initView() {
        setupCheckObserve()

        initNotificationActionAdapter()
    }

    private fun setupCheckObserve() {
        viewModel.checkUiState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> Timber.tag("notification").i("patch 성공 : ${it.data}")
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
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

        viewModel.patchCheck()
    }
}
