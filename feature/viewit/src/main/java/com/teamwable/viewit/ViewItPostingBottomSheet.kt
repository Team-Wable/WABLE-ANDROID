package com.teamwable.viewit

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.ui.base.BindingBottomSheetFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.setOnDuplicateBlockClick
import com.teamwable.ui.extensions.showKeyboard
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.type.SnackbarType
import com.teamwable.ui.util.BundleKey.IS_UPLOADED
import com.teamwable.ui.util.BundleKey.POSTING_RESULT
import com.teamwable.viewit.databinding.BottomSheetViewItPostingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewItPostingBottomSheet : BindingBottomSheetFragment<BottomSheetViewItPostingBinding>(BottomSheetViewItPostingBinding::inflate) {
    private val viewModel: ViewItPostingViewModel by viewModels()

    override fun initView() {
        val viewItSnackbar = Snackbar.make(parentFragment?.view ?: return, SnackbarType.VIEW_IT_ING)
        binding.root.context.showKeyboard(binding.etViewItLinkInput)
        collect(viewItSnackbar)
        setupTextWatchers()
        setOnLinkInputBtnClickListener()
        setOnViewItUploadBtnClickListener(viewItSnackbar)
    }

    private fun collect(snackbar: Snackbar) {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is ViewItPostingUiState.Success -> {
                        snackbar.updateToCommentComplete(SnackbarType.VIEW_IT_COMPLETE)
                        parentFragmentManager.setFragmentResult(
                            POSTING_RESULT,
                            Bundle().apply { putBoolean(IS_UPLOADED, true) },
                        )
                    }

                    is ViewItPostingUiState.Loading -> Unit
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is ViewItPostingSideEffect.ShowErrorMessage -> {
                        snackbar.updateToCommentComplete(SnackbarType.ERROR, sideEffect.message)
                    }
                }
            }
        }
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

    private fun setOnViewItUploadBtnClickListener(snackbar: Snackbar) = with(binding) {
        btnViewItContentInputUpload.setOnDuplicateBlockClick {
            snackbar.show()
            viewModel.postViewIt(etViewItLinkInputComplete.text.toString(), etViewItContentInput.text.toString())
            clearEditText()
            updateVisibilityBasedOnLinkStatus(false)
        }
    }

    private fun clearEditText() = with(binding) {
        etViewItContentInput.text.clear()
        etViewItLinkInputComplete.text.clear()
        etViewItLinkInput.text.clear()
    }
}
