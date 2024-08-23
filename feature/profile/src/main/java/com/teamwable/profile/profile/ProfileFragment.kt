package com.teamwable.profile.profile

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.model.Profile
import com.teamwable.profile.ProfileTabType
import com.teamwable.profile.ProfileUiState
import com.teamwable.profile.ProfileViewModel
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileBinding
import com.teamwable.profile.hamburger.ProfileHamburgerBottomSheet
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.load
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.Arg
import com.teamwable.ui.util.BottomSheetTag.PROFILE_HAMBURGER_BOTTOM_SHEET
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class ProfileFragment : BindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()
    private var memberId = -1L
    private var userType = ProfileUserType.AUTH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberId = requireArguments().getLong(Arg.PROFILE_USER_ID)
    }

    override fun initView() {
        viewModel.fetchAuthId(memberId)
        collect()
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle)
                .collect { uiState ->
                    when (uiState) {
                        is ProfileUiState.UserTypeDetermined -> userType = uiState.userType
                        is ProfileUiState.Success -> {
                            setLayout(uiState.profile)
                            setProfilePagerAdapter(uiState.profile)
                        }

                        else -> Unit
                    }
                }
        }
    }

    private fun setLayout(data: Profile) = with(binding) {
        initAppbarBtnVisibility()
        initAppbarHamburgerClickListener()
        viewProfileAppbar.tvProfileAppbarTitle.text = data.nickName
        ivProfileImg.load(data.profileImg)
        tvProfileNickname.text = data.nickName
        tvProfileInfo.text = getString(R.string.label_profile_info, data.teamTag, data.lckYears)
        tvProfileGhostPercentage.text = getString(R.string.label_ghost_percentage, data.ghost)
        setGhostProgress(data.ghost)
    }

    private fun initAppbarBtnVisibility() {
        with(binding.viewProfileAppbar) {
            val isAuthUser = userType == ProfileUserType.AUTH

            btnProfileAppbarBack.visible(!isAuthUser)
            btnProfileAppbarHamburger.visible(isAuthUser)
        }
    }

    private fun initAppbarHamburgerClickListener() {
        binding.viewProfileAppbar.btnProfileAppbarHamburger.setOnClickListener {
            ProfileHamburgerBottomSheet().show(childFragmentManager, PROFILE_HAMBURGER_BOTTOM_SHEET)
        }
    }

    private fun setGhostProgress(percentage: Int) {
        animateProgress(abs(100 + percentage))
        if (percentage < -50) setGhostProgressColor(com.teamwable.ui.R.color.sky_50) else setGhostProgressColor(com.teamwable.ui.R.color.purple_50)
    }

    private fun setGhostProgressColor(@ColorRes color: Int) = with(binding) {
        progressProfileGhost.setIndicatorColor(colorOf(color))
        ivProfileGhostIcon.imageTintList = ContextCompat.getColorStateList(requireContext(), color)
    }

    private fun animateProgress(targetProgress: Int) {
        val animator = ObjectAnimator.ofInt(binding.progressProfileGhost, "progress", 0, targetProgress)
        animator.duration = 1000
        animator.start()
    }

    private fun setProfilePagerAdapter(data: Profile) {
        binding.vpProfile.adapter = ProfilePagerStateAdapter(this, data.id, data.nickName, userType)
        TabLayoutMediator(
            binding.tlProfile, binding.vpProfile,
        ) { tab, position ->
            tab.text = stringOf(ProfileTabType.entries[position].label)
        }.attach()
    }
}
