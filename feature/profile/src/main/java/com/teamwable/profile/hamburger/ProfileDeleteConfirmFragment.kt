package com.teamwable.profile.hamburger

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileDeleteConfirmBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.util.Arg.DIALOG_RESULT

class ProfileDeleteConfirmFragment : BindingFragment<FragmentProfileDeleteConfirmBinding>(FragmentProfileDeleteConfirmBinding::inflate) {
    override fun initView() {
        setAppbarText()
        initBackBtnClickListener()
        initDialogDeleteBtnClickListener()
        initCheckBoxClickListener()
    }

    private fun initCheckBoxClickListener() {
        binding.cbProfileDeleteConfirm.setOnClickListener {
            binding.btnProfileDeleteConfirmNext.apply {
                isEnabled = binding.cbProfileDeleteConfirm.isChecked
                if (isEnabled) {
                    setTextColor(colorOf(com.teamwable.ui.R.color.white))
                    setOnClickListener {
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
            // Todo : 나중에 추가해야 함
        }
    }
}
