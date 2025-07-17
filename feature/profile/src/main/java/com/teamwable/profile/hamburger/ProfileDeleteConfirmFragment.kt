package com.teamwable.profile.hamburger

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamwable.common.restarter.AppReStarter
import com.teamwable.common.uistate.UiState
import com.teamwable.common.util.AmplitudeAuthTag.CLICK_NEXT_DELETEGUIDE
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileDeleteConfirmBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.util.Arg.DIALOG_RESULT
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDeleteConfirmFragment : BindingFragment<FragmentProfileDeleteConfirmBinding>(FragmentProfileDeleteConfirmBinding::inflate) {
    private val viewModel: ProfileHamburgerViewModel by viewModels()

    private val args: ProfileDeleteConfirmFragmentArgs by navArgs()
    private var reasons = emptyList<String>()

    @Inject
    lateinit var appRestarter: AppReStarter

    override fun initView() {
        reasons = args.reasons.toList()

        setAppbarText()
        initBackBtnClickListener()
        initDialogDeleteBtnClickListener()
        initCheckBoxClickListener()

        setupWithdrawalObserve()
    }

    private fun setupWithdrawalObserve() {
        viewModel.withdrawalUiState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    Timber.tag("withdrawal").i("patch 성공 : ${it.data}")
                    viewModel.clearInfo()
                    navigateToSplashScreen()
                }

                is UiState.Failure -> navigateToErrorFragment()
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun navigateToErrorFragment() {
        (activity as Navigation).navigateToErrorFragment()
    }

    private fun navigateToSplashScreen() {
        appRestarter.restartApp()
    }

    private fun initCheckBoxClickListener() {
        binding.cbProfileDeleteConfirm.setOnClickListener {
            binding.btnProfileDeleteConfirmNext.apply {
                isEnabled = binding.cbProfileDeleteConfirm.isChecked
                if (isEnabled) {
                    setTextColor(colorOf(com.teamwable.ui.R.color.white))
                    setOnClickListener {
                        trackEvent(CLICK_NEXT_DELETEGUIDE)
                        showToProfileDeleteDialogFragment()
                    }
                } else {
                    setTextColor(colorOf(com.teamwable.ui.R.color.gray_600))
                }
            }
        }
    }

    private fun setAppbarText() {
        binding.viewProfileDeleteConfirmAppbar.tvProfileAppbarTitle.text = stringOf(R.string.appbar_profile_delete_title)
    }

    private fun initBackBtnClickListener() {
        binding.viewProfileDeleteConfirmAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showToProfileDeleteDialogFragment() {
        TwoButtonDialog.Companion.show(requireContext(), findNavController(), DialogType.DELETE_ACCOUNT)
    }

    private fun initDialogDeleteBtnClickListener() {
        parentFragmentManager.setFragmentResultListener(DIALOG_RESULT, viewLifecycleOwner) { key, bundle ->
            viewModel.patchCheck(reasons)
        }
    }
}
