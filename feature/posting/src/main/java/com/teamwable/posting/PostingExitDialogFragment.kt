package com.teamwable.posting

import androidx.navigation.fragment.findNavController
import com.teamwable.posting.databinding.FragmentPostingExitDialogBinding
import com.teamwable.ui.base.BindingDialogFragment
import com.teamwable.ui.extensions.dialogFragmentResize

class PostingExitDialogFragment : BindingDialogFragment<FragmentPostingExitDialogBinding>(R.layout.fragment_posting_exit_dialog, FragmentPostingExitDialogBinding::inflate) {
    override fun initView() {
        initCancelBtnClickListener()
        initExitBtnClickListener()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 30.0f)
    }

    private fun initCancelBtnClickListener() {
        binding.btnPostingExitDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initExitBtnClickListener() {
        binding.btnPostingExitDialogExit.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
