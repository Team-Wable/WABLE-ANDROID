package com.teamwable.profile

import android.view.View
import com.teamwable.profile.databinding.FragmentProfileBinding
import com.teamwable.ui.base.BindingFragment
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
            btnProfileAppbarBack.visibility = View.GONE
            btnProfileAppbarHamburger.visibility = View.VISIBLE
        }
    }

    private fun initAppbarHamburgerClickListener() {
        binding.viewProfileAppbar.btnProfileAppbarHamburger.setOnClickListener {
            ProfileHamburgerBottomSheet().show(childFragmentManager, PROFILE_HAMBURGER_BOTTOM_SHEET)
        }
    }

    companion object {
        const val PROFILE_HAMBURGER_BOTTOM_SHEET = "ProfileHamburgerBottomSheet"
    }
}
