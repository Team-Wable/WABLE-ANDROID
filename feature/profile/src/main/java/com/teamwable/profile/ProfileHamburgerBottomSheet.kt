package com.teamwable.profile

import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.BottomsheetProfileHamburgerBinding
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.util.Arg.DIALOG_RESULT

class ProfileHamburgerBottomSheet : BindingBottomSheetFragment<BottomsheetProfileHamburgerBinding>(BottomsheetProfileHamburgerBinding::inflate) {
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
            // Todo : 나중에 추가해야 함
        }
    }

    private fun navigateToWeb(uri: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(uri)).also { startActivity(it) }
    }
}
