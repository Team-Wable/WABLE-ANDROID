package com.teamwable.profile

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.FragmentProfileDeleteConfirmBinding
import com.teamwable.ui.base.BindingFragment

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
                    setTextColor(context.getColor(com.teamwable.ui.R.color.white))
                    setOnClickListener {
                        showToProfileDeleteDialogFragment()
                    }
                } else {
                    setTextColor(context.getColor(com.teamwable.ui.R.color.gray_600))
                }
            }
        }
    }

    private fun setAppbarText() {
        binding.viewProfileDeleteConfirmAppbar.tvProfileAppbarTitle.text = getString(R.string.appbar_profile_delete_title)
    }

    private fun initBackBtnClickListener() {
        binding.viewProfileDeleteConfirmAppbar.btnProfileAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showToProfileDeleteDialogFragment() {
        ProfileDeleteDialogFragment().show(childFragmentManager, PROFILE_DELETE_DIALOG)
    }

    companion object {
        const val PROFILE_DELETE_DIALOG = "ProfileDeleteDialog"
    }
}
