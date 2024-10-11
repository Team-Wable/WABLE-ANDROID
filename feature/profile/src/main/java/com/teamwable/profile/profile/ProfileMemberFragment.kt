package com.teamwable.profile.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamwable.model.Profile
import com.teamwable.profile.profiletabs.ProfilePagerStateAdapter
import com.teamwable.profile.profiletabs.ProfileTabType
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.Arg
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileMemberFragment : BindingProfileFragment() {
    private val viewModel: ProfileMemberViewModel by viewModels()

    private var userId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong(Arg.PROFILE_USER_ID) ?: return
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnProfileEdit.visible(false)
        initBackBtnClickListener()
        viewModel.fetchProfileInfo(userId)
        collect()
        setSwipeLayout()
    }

    private fun initBackBtnClickListener() {
        binding.viewProfileAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is ProfileMemberUiState.Success -> {
                        setLayout(uiState.profile)
                        setProfilePagerAdapter(uiState.profile)
                    }

                    is ProfileMemberUiState.Error -> (activity as Navigation).navigateToErrorFragment()
                    is ProfileMemberUiState.Loading -> Unit
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
            viewModel.fetchProfileInfo(userId)
        }
    }

    private fun setProfilePagerAdapter(data: Profile) {
        binding.vpProfile.adapter = ProfilePagerStateAdapter(this, data.id, data.nickName, ProfileUserType.MEMBER)
        TabLayoutMediator(
            binding.tlProfile, binding.vpProfile,
        ) { tab, position ->
            tab.text = stringOf(ProfileTabType.entries[position].label)
        }.attach()
    }
}
