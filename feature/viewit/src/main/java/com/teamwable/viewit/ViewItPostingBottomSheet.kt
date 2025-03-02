package com.teamwable.viewit

import android.content.res.ColorStateList
import android.util.Patterns
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.showKeyboard
import com.teamwable.ui.extensions.visible
import com.teamwable.viewit.databinding.BottomSheetViewItPostingBinding

class ViewItPostingBottomSheet : BindingBottomSheetFragment<BottomSheetViewItPostingBinding>(BottomSheetViewItPostingBinding::inflate) {
    override fun initView() {
        binding.root.context.showKeyboard(binding.etViewItLinkInput)
        validateLinkInput()
        setOnLinkInputBtnClickListener()
    }

    private fun validateLinkInput() = with(binding.etViewItLinkInput) {
        doAfterTextChanged {
            binding.btnViewItLinkInputUpload.isEnabled = validateLinkUrl(text.toString()) && text.isNotEmpty() && text.length < 60
            val color = if (text.isNotEmpty()) ColorStateList.valueOf(colorOf(com.teamwable.ui.R.color.blue_10))
            else ColorStateList.valueOf(colorOf(com.teamwable.ui.R.color.gray_100))
            setEditTextBgColor(this, color)
        }
    }

    private fun validateLinkUrl(url: String): Boolean {
        val isLinkValid = Patterns.WEB_URL.matcher(url.trim()).matches()
        return isLinkValid
    }

    private fun setEditTextBgColor(
        view: EditText,
        backgroundTintResId: ColorStateList,
    ) {
        view.backgroundTintList = backgroundTintResId
    }

    private fun setOnLinkInputBtnClickListener() = with(binding) {
        btnViewItLinkInputUpload.setOnClickListener {
            groupViewItLink.visible(false)
            groupViewItContent.visible(true)
            etViewItLinkInputComplete.text = binding.etViewItLinkInput.text
            etViewItContentInput.requestFocus()
        }
    }
}
