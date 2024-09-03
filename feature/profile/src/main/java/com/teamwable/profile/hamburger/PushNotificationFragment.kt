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
import com.teamwable.profile.databinding.FragmentPushNotificationBinding
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
class PushNotificationFragment : BindingFragment<FragmentPushNotificationBinding>(FragmentPushNotificationBinding::inflate) {
    private val viewModel by viewModels<ProfileHamburgerViewModel>()

    override fun initView() {
        setAppbarText()
        setPushNotificationText()

        initPushAlarmSettingClickListener()
        initBackBtnClickListener()

        setupUserPushNotificationInfoObserve()
    }

    override fun onResume() {
        super.onResume()
        refreshPushNotificationPermission()
    }

    private fun refreshPushNotificationPermission() {
        setPushNotificationText()
        when (checkPushNotificationAllowed()) {
            true -> handlePushNotificationPermissionGranted()
            false -> handlePushNotificationPermissionDenied()
        }
    }

    private fun handlePushNotificationPermissionGranted() {
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

    private fun handlePushNotificationPermissionDenied() {
        viewModel.patchUserProfileUri(MemberInfoEditModel(isPushAlarmAllowed = false))
    }

    private fun setupUserPushNotificationInfoObserve() {
        viewModel.pushNotificationAllowedState.flowWithLifecycle(viewLifeCycle).onEach {
            viewModel.saveIsPushNotificationAllowed(it)
        }.launchIn(viewLifeCycleScope)
    }

    private fun initPushAlarmSettingClickListener() {
        binding.tvPushNotificationContent.setOnClickListener {
            navigateToAppSettings()
        }
        binding.btnPushNotificationMore.setOnClickListener {
            navigateToAppSettings()
        }
    }


    private fun setPushNotificationText() {
        binding.tvPushNotificationContent.text =
            if (checkPushNotificationAllowed()) getString(R.string.tv_push_notification_content_on)
            else getString(R.string.tv_push_notification_content_off)
    }

    private fun checkPushNotificationAllowed(): Boolean {
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
        binding.viewPushNotificationAppbar.tvProfileAppbarTitle.text = stringOf(R.string.appbar_push_notification_title)
    }

    private fun initBackBtnClickListener() {
        binding.viewPushNotificationAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
