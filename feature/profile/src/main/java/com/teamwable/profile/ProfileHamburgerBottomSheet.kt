package com.teamwable.profile

import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.BottomsheetProfileHamburgerBinding
import com.teamwable.ui.base.BindingBottomSheetFragment

class ProfileHamburgerBottomSheet : BindingBottomSheetFragment<BottomsheetProfileHamburgerBinding>(BottomsheetProfileHamburgerBinding::inflate) {
    override fun initView() {
        initAccountInformationBtnClickListener()
        initNotificationSettingBtnClickListener()
        initFeedbackBtnClickListener()
        initCustomerServiceBtnClickListener()
        initLogoutBtnClickListener()
    }

    private fun initAccountInformationBtnClickListener() {
        binding.tvHamburgerAccountInformation.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_hamburger_to_navigation_profile_information)
        }
    }

    private fun initNotificationSettingBtnClickListener() {
        binding.tvHamburgerNotificationSetting.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_hamburger_to_navigation_push_notification)
        }
    }

    private fun initFeedbackBtnClickListener() {
        // navigateToWeb("")
    }

    private fun initCustomerServiceBtnClickListener() {
        // navigateToWeb("")
    }

    private fun initLogoutBtnClickListener() {
        binding.tvHamburgerLogout.setOnClickListener {
            LogoutDialogFragment().show(childFragmentManager, LOGOUT_DIALOG)
        }
    }

    private fun navigateToWeb(uri: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(uri)).also { startActivity(it) }
    }

    companion object {
        const val LOGOUT_DIALOG = "LogoutDialog"
    }
}
