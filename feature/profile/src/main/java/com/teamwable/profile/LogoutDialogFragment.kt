package com.teamwable.profile

import com.teamwable.profile.databinding.FragmentDeleteDialogBinding
import com.teamwable.ui.base.BindingDialogFragment
import com.teamwable.ui.extensions.dialogFragmentResize

class LogoutDialogFragment : BindingDialogFragment<FragmentDeleteDialogBinding>(R.layout.fragment_delete_dialog, FragmentDeleteDialogBinding::inflate) {
    override fun initView() {
        initText()
        initCancelBtnClickListener()
        initDeleteBtnClickListener()
    }

    private fun initText() {
        with(binding) {
            tvDeleteDialogTitle.text = "로그아웃하시겠어요?"
            btnDeleteDialogDelete.text = "로그아웃하기"
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 30.0f)
    }

    private fun initCancelBtnClickListener() {
        binding.btnDeleteDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initDeleteBtnClickListener() {
        binding.btnDeleteDialogDelete.setOnClickListener {
            // Todo : 나중에 추가해야 함
        }
    }
}
