package com.teamwable.profile

import androidx.navigation.fragment.findNavController
import com.teamwable.profile.databinding.FragmentProfileDeleteReasonBinding
import com.teamwable.ui.base.BindingFragment

class ProfileDeleteReasonFragment : BindingFragment<FragmentProfileDeleteReasonBinding>(FragmentProfileDeleteReasonBinding::inflate) {
    override fun initView() {
        setAppbarText()
        initNextBtnClickListener()
    }

    private fun setAppbarText() {
        binding.viewProfileDeleteReasonAppbar.tvProfileAppbarTitle.text = "계정 삭제"
    }

    private fun initNextBtnClickListener() {
        binding.btnProfileDeleteReasonNext.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_delete_reason_to_navigation_profile_delete_confirm)
        }
    }
}
