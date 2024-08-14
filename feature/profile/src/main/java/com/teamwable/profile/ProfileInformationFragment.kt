package com.teamwable.profile

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.FragmentProfileInformationBinding
import com.teamwable.ui.base.BindingFragment

class ProfileInformationFragment : BindingFragment<FragmentProfileInformationBinding>(FragmentProfileInformationBinding::inflate) {
    override fun initView() {
        setAppbarText()
        initDeleteBtnClickListener()
    }

    private fun setAppbarText() {
        binding.viewProfileInformationAppbar.tvProfileAppbarTitle.text = "계정 정보"
    }

    private fun initDeleteBtnClickListener() {
        binding.tvProfileInformationDelete.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_information_to_navigation_profile_delete_reason)
        }
    }
}
