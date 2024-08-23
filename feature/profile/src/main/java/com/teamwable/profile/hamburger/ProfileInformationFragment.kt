package com.teamwable.profile.hamburger

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.teamwable.common.uistate.UiState
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.profile.ProfileViewModel
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileInformationBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileInformationFragment : BindingFragment<FragmentProfileInformationBinding>(FragmentProfileInformationBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()

    override fun initView() {
        viewModel.getMemberData()

        setAppbarText()

        setupMemberDataObserve()

        initBackBtnClickListener()
        initDeleteBtnClickListener()
    }

    private fun setupMemberDataObserve() {
        viewModel.memberDataUiState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> setInformationText(it.data)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setInformationText(memberData: MemberDataModel) {
        with(binding) {
            tvProfileInformationSocialContent.text = memberData.socialPlatform
            tvProfileInformationVersionContent.text = memberData.versionInformation
            tvProfileInformationIdContent.text = memberData.showMemberId
            tvProfileInformationRegistrationDateContent.text = memberData.joinDate
        }
    }

    private fun setAppbarText() {
        binding.viewProfileInformationAppbar.tvProfileAppbarTitle.text = stringOf(R.string.appbar_profile_information_title)
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
