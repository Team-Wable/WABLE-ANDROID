package com.teamwable.profile

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.FragmentProfileInformationBinding
import com.teamwable.ui.base.BindingFragment

class ProfileInformationFragment : BindingFragment<FragmentProfileInformationBinding>(FragmentProfileInformationBinding::inflate) {
    override fun initView() {
        setAppbarText()
        setInformationText()
        initBackBtnClickListener()
        initDeleteBtnClickListener()
    }

    private fun setInformationText() {
        with(binding) {
            tvProfileInformationSocialContent.text = "카카오톡 소셜 로긘"
            tvProfileInformationVersionContent.text = "0.0.0"
            tvProfileInformationIdContent.text = "wable"
            tvProfileInformationRegistrationDateContent.text = "2024-08-15"
        }
    }

    private fun setAppbarText() {
        binding.viewProfileInformationAppbar.tvProfileAppbarTitle.text = getString(R.string.appbar_profile_information_title)
    }

    private fun initBackBtnClickListener() {
        binding.viewProfileInformationAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initDeleteBtnClickListener() {
        binding.tvProfileInformationDelete.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_information_to_navigation_profile_delete_reason)
        }
    }
}
