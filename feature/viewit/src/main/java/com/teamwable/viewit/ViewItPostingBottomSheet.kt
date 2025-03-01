package com.teamwable.viewit

import android.text.util.Linkify
import androidx.core.widget.doAfterTextChanged
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.extensions.showKeyboard
import com.teamwable.ui.extensions.visible
import com.teamwable.viewit.databinding.BottomSheetViewItPostingBinding

class ViewItPostingBottomSheet : BindingBottomSheetFragment<BottomSheetViewItPostingBinding>(BottomSheetViewItPostingBinding::inflate) {
    override fun initView() {
        binding.ibViewItLinkInputUpload.isEnabled = false
        binding.root.context.showKeyboard(binding.etViewItLinkInput)
        checkLinkValidation()
        setOnLinkInputBtnClickListener()
    }

    private fun checkLinkValidation() {
        binding.etViewItLinkInput.doAfterTextChanged {
            val isLinkValid = Linkify.addLinks(binding.etViewItLinkInput, Linkify.WEB_URLS)
            binding.ibViewItLinkInputUpload.isEnabled = isLinkValid
        }
    }

    private fun setOnLinkInputBtnClickListener() = with(binding) {
        ibViewItLinkInputUpload.setOnClickListener {
            groupViewItLink.visible(false)
            groupViewItContent.visible(true)
            etViewItLinkInputComplete.text = binding.etViewItLinkInput.text
            etViewItContentInput.requestFocus()
        }
    }
}
