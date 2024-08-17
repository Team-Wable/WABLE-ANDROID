package com.teamwable.profile

import com.teamwable.profile.databinding.FragmentProfileBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.util.BottomSheetTag.PROFILE_HAMBURGER_BOTTOM_SHEET
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override fun initView() {
        initAppbarBtnVisibility()
        setAppbarText()
        initAppbarHamburgerClickListener()
    }

    private fun setAppbarText() {
        binding.viewProfileAppbar.tvProfileAppbarTitle.text = "배 차은우"
    }

    private fun initAppbarBtnVisibility() {
        with(binding.viewProfileAppbar) {
            btnProfileAppbarBack.visible(false)
            btnProfileAppbarHamburger.visible(true)
        }
    }

    private fun initAppbarHamburgerClickListener() {
        binding.viewProfileAppbar.btnProfileAppbarHamburger.setOnClickListener {
            ProfileHamburgerBottomSheet().show(childFragmentManager, PROFILE_HAMBURGER_BOTTOM_SHEET)
        }
    }
}
