package com.teamwable.profile.hamburger

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teamwable.profile.R
import com.teamwable.profile.databinding.BottomsheetProfileHamburgerBinding
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.util.Arg.DIALOG_RESULT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileHamburgerBottomSheet : BindingBottomSheetFragment<BottomsheetProfileHamburgerBinding>(BottomsheetProfileHamburgerBinding::inflate) {
    private val viewModel: ProfileHamburgerViewModel by viewModels()

    override fun initView() {
        initAccountInformationBtnClickListener()
        initNotificationSettingBtnClickListener()
        initFeedbackBtnClickListener()
        initCustomerServiceBtnClickListener()
        initLogoutBtnClickListener()
        initDialogDeleteBtnClickListener()
    }

    private fun initAccountInformationBtnClickListener() {
        binding.tvProfileHamburgerAccountInformation.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_profile_information)
        }
    }

    private fun initNotificationSettingBtnClickListener() {
        binding.tvProfileHamburgerNotificationSetting.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_push_notification)
        }
    }

    private fun initFeedbackBtnClickListener() {
        // navigateToWeb("")
    }

    private fun initCustomerServiceBtnClickListener() {
        // navigateToWeb("")
    }

    private fun initLogoutBtnClickListener() {
        binding.tvProfileHamburgerLogout.setOnClickListener {
            TwoButtonDialog.Companion.show(requireContext(), findNavController(), DialogType.LOGOUT)
        }
    }

    private fun initDialogDeleteBtnClickListener() {
        parentFragmentManager.setFragmentResultListener(DIALOG_RESULT, viewLifecycleOwner) { key, bundle ->
            viewLifeCycleScope.launch {
                viewModel.saveIsAutoLogin(false)
                navigateToSplashScreen()
            }
        }
    }

    private fun navigateToSplashScreen() {
        startActivity(
            Intent.makeRestartActivityTask(
                requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)?.component,
            ),
        )
    }

    private fun navigateToWeb(uri: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(uri)).also { startActivity(it) }
    }
}
