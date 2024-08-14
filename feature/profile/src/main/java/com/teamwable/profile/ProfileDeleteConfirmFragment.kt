package com.teamwable.profile

import com.teamwable.profile.databinding.FragmentProfileDeleteConfirmBinding
import com.teamwable.ui.base.BindingFragment

class ProfileDeleteConfirmFragment : BindingFragment<FragmentProfileDeleteConfirmBinding>(FragmentProfileDeleteConfirmBinding::inflate) {
    override fun initView() {
        setAppbarText()
        initNextBtnClickListener()
    }

    private fun setAppbarText() {
        binding.viewProfileDeleteConfirmAppbar.tvProfileAppbarTitle.text = "계정 삭제"
    }

    private fun initNextBtnClickListener() {
        binding.btnProfileDeleteConfirmNext.setOnClickListener {
            ProfileDeleteDialogFragment().show(childFragmentManager, PROFILE_DELETE_DIALOG)
        }
    }

    companion object {
        const val PROFILE_DELETE_DIALOG = "ProfileDeleteDialog"
    }
}
