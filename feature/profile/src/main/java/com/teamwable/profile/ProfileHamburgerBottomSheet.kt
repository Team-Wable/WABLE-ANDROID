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
