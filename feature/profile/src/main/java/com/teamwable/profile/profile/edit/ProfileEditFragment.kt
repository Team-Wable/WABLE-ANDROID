package com.teamwable.profile.profile.edit

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileEditBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.util.Arg.PROFILE_EDIT_RESULT

class ProfileEditFragment : BindingFragment<FragmentProfileEditBinding>(FragmentProfileEditBinding::inflate) {
    private val args: ProfileEditFragmentArgs by navArgs()
    private val profile: MemberInfoEditModel by lazy {
        args.memberProfileModel
    }

    override fun initView() {
        initProfileEditAppBar()
        initComposeView()
    }

    private fun initProfileEditAppBar() {
        binding.viewProfileEditAppbar.apply {
            btnProfileAppbarBack.setOnClickListener { findNavController().popBackStack() }
            tvProfileAppbarTitle.text = getString(R.string.profile_edit_app_bar)
        }
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    ProfileEditRoute(
                        profile = profile,
                        navigateToProfile = { updatedProfile -> saveAndNavigateBack(updatedProfile) },
                    )
                }
            }
        }
    }

    private fun saveAndNavigateBack(updatedProfile: MemberInfoEditModel) {
        setFragmentResult(
            PROFILE_EDIT_RESULT,
            bundleOf(PROFILE_EDIT_RESULT to updatedProfile),
        )
        findNavController().popBackStack()
    }
}
