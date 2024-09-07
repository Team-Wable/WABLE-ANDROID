package com.teamwable.profile.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.model.Profile
import com.teamwable.profile.hamburger.ProfileHamburgerBottomSheet
import com.teamwable.profile.profiletabs.ProfilePagerStateAdapter
import com.teamwable.profile.profiletabs.ProfileTabType
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.BottomSheetTag.PROFILE_HAMBURGER_BOTTOM_SHEET
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileAuthFragment : BindingProfileFragment() {
    private val viewModel: ProfileAuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppbar()
        initAppbarHamburgerClickListener()
        collect()
        setSwipeLayout()
    }

    private fun setAppbar() {
        binding.viewProfileAppbar.btnProfileAppbarHamburger.visible(true)
        binding.viewProfileAppbar.btnProfileAppbarBack.visible(false)
    }

    private fun initAppbarHamburgerClickListener() {
        binding.viewProfileAppbar.btnProfileAppbarHamburger.setOnClickListener {
            ProfileHamburgerBottomSheet().show(childFragmentManager, PROFILE_HAMBURGER_BOTTOM_SHEET)
        }
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is ProfileAuthUiState.Success -> {
                        setLayout(uiState.profile)
                        setProfilePagerAdapter(uiState.profile)
                    }

                    is ProfileAuthUiState.Error -> (activity as Navigation).navigateToErrorFragment()
                    is ProfileAuthUiState.Loading -> Unit
                }
            }
        }
    }

    private fun setSwipeLayout() {
        binding.appbarProfileInfo.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                binding.layoutProfileSwipe.isEnabled = verticalOffset == 0
            },
        )

        binding.layoutProfileSwipe.setOnRefreshListener {
            binding.layoutProfileSwipe.isRefreshing = false
            viewModel.fetchAuthId()
        }
    }

    private fun setProfilePagerAdapter(data: Profile) {
        binding.vpProfile.adapter = ProfilePagerStateAdapter(this, data.id, data.nickName, ProfileUserType.AUTH)
        TabLayoutMediator(
            binding.tlProfile, binding.vpProfile,
        ) { tab, position ->
            tab.text = stringOf(ProfileTabType.entries[position].label)
        }.attach()
    }
}
