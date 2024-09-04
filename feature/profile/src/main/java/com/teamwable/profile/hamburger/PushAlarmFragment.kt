package com.teamwable.profile.hamburger

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentPushAlarmBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.navigateToAppSettings
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class PushAlarmFragment : BindingFragment<FragmentPushAlarmBinding>(FragmentPushAlarmBinding::inflate) {
    private val viewModel by viewModels<ProfileHamburgerViewModel>()

    override fun initView() {
        setAppbarText()
        setPushAlarmText()

        initPushAlarmSettingClickListener()
        initBackBtnClickListener()

        setupUserPushAlarmInfoObserve()
    }

    override fun onResume() {
        super.onResume()
        refreshPushAlarmPermission()
    }

    private fun refreshPushAlarmPermission() {
        setPushAlarmText()
        when (checkPushAlarmAllowed()) {
            true -> handlePushAlarmPermissionGranted()
            false -> handlePushAlarmPermissionDenied()
        }
    }

    private fun handlePushAlarmPermissionGranted() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.patchUserProfileUri(
                        MemberInfoEditModel(
                            isPushAlarmAllowed = true,
                            fcmToken = task.result
                        )
                    )
                    Timber.tag("fcm").d("fcm token: $task.result")
                } else {
                    Timber.d(task.exception)
                    return@OnCompleteListener
                }
            }
        )
    }

    private fun handlePushAlarmPermissionDenied() {
        viewModel.patchUserProfileUri(MemberInfoEditModel(isPushAlarmAllowed = false))
    }

    private fun setupUserPushAlarmInfoObserve() {
        viewModel.pushAlarmAllowedState.flowWithLifecycle(viewLifeCycle).onEach {
            viewModel.saveIsPushAlarmAllowed(it)
        }.launchIn(viewLifeCycleScope)
    }

    private fun initPushAlarmSettingClickListener() {
        binding.tvPushAlarmContent.setOnClickListener {
            requireContext().navigateToAppSettings()
        }
        binding.btnPushAlarmMore.setOnClickListener {
            requireContext().navigateToAppSettings()
        }
    }


    private fun setPushAlarmText() {
        binding.tvPushAlarmContent.text =
            if (checkPushAlarmAllowed()) getString(R.string.tv_push_alarm_content_on)
            else getString(R.string.tv_push_alarm_content_off)
    }

    private fun checkPushAlarmAllowed(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
        }
    }

    private fun setAppbarText() {
        binding.viewPushAlarmAppbar.tvProfileAppbarTitle.text = stringOf(R.string.appbar_push_alarm_title)
    }

    private fun initBackBtnClickListener() {
        binding.viewPushAlarmAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
