package com.teamwable.profile.hamburger

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teamwable.common.restarter.AppReStarter
import com.teamwable.profile.R
import com.teamwable.profile.databinding.BottomsheetProfileHamburgerBinding
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.extensions.openUri
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.util.Arg.DIALOG_RESULT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileHamburgerBottomSheet : BindingBottomSheetFragment<BottomsheetProfileHamburgerBinding>(BottomsheetProfileHamburgerBinding::inflate) {
    private val viewModel: ProfileHamburgerViewModel by viewModels()

    @Inject
    lateinit var appRestarter: AppReStarter

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
        binding.tvProfileHamburgerFeedback.setOnClickListener {
            openUri("https://forms.gle/WWfbHXvGNgXMxgZr5")
        }
    }

    private fun initCustomerServiceBtnClickListener() {
        binding.tvProfileHamburgerCustomerService.setOnClickListener {
            openUri("https://forms.gle/WWfbHXvGNgXMxgZr5")
        }
    }

    private fun initLogoutBtnClickListener() {
        binding.tvProfileHamburgerLogout.setOnClickListener {
            TwoButtonDialog.Companion.show(requireContext(), findNavController(), DialogType.LOGOUT)
        }
    }

    private fun initDialogDeleteBtnClickListener() {
        parentFragment?.parentFragmentManager?.setFragmentResultListener(DIALOG_RESULT, viewLifecycleOwner) { key, bundle ->
            viewModel.saveIsAutoLogin(false)
            navigateToSplashScreen()
        }
    }

    private fun navigateToSplashScreen() {
        appRestarter.restartApp()
    }
}
