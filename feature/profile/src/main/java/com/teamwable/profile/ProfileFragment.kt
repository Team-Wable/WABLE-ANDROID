package com.teamwable.profile

import android.animation.ObjectAnimator
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.model.Profile
import com.teamwable.profile.databinding.FragmentProfileBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.load
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.util.BottomSheetTag.PROFILE_HAMBURGER_BOTTOM_SHEET
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class ProfileFragment : BindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()

    override fun initView() {
        initAppbarBtnVisibility()
        setAppbarText()
        initAppbarHamburgerClickListener()
        setLayout()
        setProfilePagerAdapter()
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

    private fun setLayout() = with(binding) {
        ivProfileImg.load(mock.profileImg)
        tvProfileNickname.text = mock.nickName
        tvProfileInfo.text = getString(R.string.label_profile_info, mock.teamTag, mock.lckYears)
        tvProfileGhostPercentage.text = getString(R.string.label_ghost_percentage, mock.ghost)
        setGhostProgress(mock.ghost)
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle)
                .collect { uiState ->
                    when (uiState) {
                        is ProfileUiState.FetchUserId -> Unit
                        else -> Unit
                    }
                }
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

    private fun setProfilePagerAdapter() {
        binding.vpProfile.adapter = ProfilePagerStateAdapter(this, mock.id, mock.nickName, ProfileUserType.AUTH)
        TabLayoutMediator(
            binding.tlProfile, binding.vpProfile,
        ) { tab, position ->
            tab.text = stringOf(ProfileTabType.entries[position].label)
        }.attach()
    }

    // TODO : mock data 지우기
    companion object {
        val mock = Profile(
            id = 7,
            nickName = "배 차은우",
            profileImg = "https://github.com/user-attachments/assets/66fdd6f1-c0c5-4438-81f4-bea09b09acd1",
            intro = "",
            ghost = -10,
            teamTag = "T1",
            lckYears = 2022,
            level = 1,
        )
    }
}
