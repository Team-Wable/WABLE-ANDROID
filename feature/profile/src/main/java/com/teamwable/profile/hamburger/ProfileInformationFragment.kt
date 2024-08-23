package com.teamwable.profile.hamburger

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileInformationBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.stringOf
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
        viewModel.memberDataUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> setInformationText(it.data)
                else -> Unit
            }
        }.launchIn(lifecycleScope)
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
