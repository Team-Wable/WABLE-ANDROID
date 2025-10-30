package com.teamwable.viewit.posting

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.component.TwoButtonDialog
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.setOnDuplicateBlockClick
import com.teamwable.ui.extensions.showKeyboard
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.util.Arg.DIALOG_RESULT
import com.teamwable.ui.util.BundleKey.POSTING_RESULT
import com.teamwable.ui.util.BundleKey.VIEW_IT_CONTENT
import com.teamwable.ui.util.BundleKey.VIEW_IT_LINK
import com.teamwable.viewit.databinding.BottomSheetViewItPostingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewItPostingBottomSheet : BindingBottomSheetFragment<BottomSheetViewItPostingBinding>(BottomSheetViewItPostingBinding::inflate) {
    override fun initView() {
        binding.root.context.showKeyboard(binding.etViewItLinkInput)
        setupTextWatchers()
        setOnLinkInputBtnClickListener()
        setOnViewItUploadBtnClickListener()
        initOutSideTouchClickListener()
        initDialogExitBtnClickListener()
    }

    private fun setupTextWatchers() {
        binding.etViewItLinkInput.doAfterTextChanged { updateLinkInputState() }
        binding.etViewItContentInput.doAfterTextChanged { updateContentInputState() }
        binding.etViewItLinkInputComplete.doAfterTextChanged { updateContentInputState() }
    }

    private fun updateLinkInputState() {
        val linkText = binding.etViewItLinkInput.text.toString()
        binding.btnViewItLinkInputUpload.isEnabled = validateLinkUrl(linkText)
        updateEditTextBackground(binding.etViewItLinkInput, ColorStateList.valueOf(colorOf(com.teamwable.ui.R.color.blue_10)))
    }

    private fun updateContentInputState() {
        val contentText = binding.etViewItContentInput.text.toString()
        val linkText = binding.etViewItLinkInputComplete.text.toString()

        binding.btnViewItContentInputUpload.isEnabled = contentText.isNotEmpty() && validateLinkUrl(linkText)
        updateEditTextBackground(binding.etViewItContentInput, null)
    }

    private fun validateLinkUrl(url: String) = Patterns.WEB_URL.matcher(url.trim()).matches()

    private fun updateEditTextBackground(view: EditText, activeColor: ColorStateList?) {
        val color = if (view.text.isNullOrEmpty()) ColorStateList.valueOf(colorOf(com.teamwable.ui.R.color.gray_100))
        else activeColor
        view.backgroundTintList = color
    }

    private fun setOnLinkInputBtnClickListener() = with(binding) {
        btnViewItLinkInputUpload.setOnClickListener {
            updateVisibilityBasedOnLinkStatus(true)
            etViewItLinkInputComplete.text = binding.etViewItLinkInput.text
            etViewItContentInput.requestFocus()
        }
    }

    private fun updateVisibilityBasedOnLinkStatus(isLinkCompleted: Boolean) = with(binding) {
        groupViewItLink.visible(!isLinkCompleted)
        groupViewItContent.visible(isLinkCompleted)
    }

    private fun setOnViewItUploadBtnClickListener() = with(binding) {
        btnViewItContentInputUpload.setOnDuplicateBlockClick {
            setUploadResult()
            dismiss()
        }
    }

    private fun setUploadResult() = with(binding) {
        parentFragmentManager.setFragmentResult(
            POSTING_RESULT,
            Bundle().apply {
                putString(VIEW_IT_LINK, etViewItLinkInputComplete.text.toString())
                putString(VIEW_IT_CONTENT, etViewItContentInput.text.toString())
            },
        )
    }

    private fun initOutSideTouchClickListener() {
        val bottomSheetDialog = dialog as BottomSheetDialog
        val touchOutsideView = bottomSheetDialog.window?.decorView?.findViewById<View>(com.google.android.material.R.id.touch_outside)

        touchOutsideView?.setOnClickListener { handleShowCancelDialog() }
    }

    private fun handleShowCancelDialog() {
        if (binding.etViewItLinkInput.text.isEmpty()) dismiss()
        else TwoButtonDialog.show(requireContext(), findNavController(), DialogType.CANCEL_POSTING)
    }

    private fun initDialogExitBtnClickListener() {
        parentFragmentManager.setFragmentResultListener(DIALOG_RESULT, viewLifecycleOwner) { _, _ ->
            findNavController().popBackStack()
        }
    }
}
