package com.teamwable.profile

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.FragmentProfileDeleteConfirmBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.util.DialogTag.PROFILE_DELETE_DIALOG

class ProfileDeleteConfirmFragment : BindingFragment<FragmentProfileDeleteConfirmBinding>(FragmentProfileDeleteConfirmBinding::inflate) {
    override fun initView() {
        setAppbarText()
        initBackBtnClickListener()
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
        ProfileDeleteDialogFragment().show(childFragmentManager, PROFILE_DELETE_DIALOG)
    }
}
