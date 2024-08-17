package com.teamwable.posting

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import coil.load
import com.teamwable.posting.databinding.FragmentPostingBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.showPermissionAppSettingsDialog
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.util.DialogTag.POSTING_EXIT_DIALOG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PostingFragment : BindingFragment<FragmentPostingBinding>(FragmentPostingBinding::inflate) {
    private val viewModel by viewModels<PostingViewModel>()

    private var totalContentLength = 0

    private lateinit var getGalleryLauncher: ActivityResultLauncher<String>
    private lateinit var getPhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> {
                    try {
                        selectImage()
                    } catch (e: Exception) {
                        toast("에러 : ${e.message}")
                    }
                }

                false -> handlePermissionDenied()
            }
        }

    private fun handlePermissionDenied() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
            context?.showPermissionAppSettingsDialog()
        }
    }

    override fun initView() {
        showKeyboard()

        initBackBtnClickListener()
        initEditTextBtn()

        initPhotoBtnClickListener()
        initGalleryLauncher()
        initPhotoPickerLauncher()
        observePhotoUri()
    }

    private fun initPhotoPickerLauncher() {
        getPhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { imageUri ->
                imageUri?.let { viewModel.setPhotoUri(it.toString()) }
            }
    }

    private fun initGalleryLauncher() {
        getGalleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
                imageUri?.let { viewModel.setPhotoUri(it.toString()) }
            }
    }

    private fun initPhotoBtnClickListener() = with(binding) {
        ivPostingPhotoBtn.setOnClickListener {
            getGalleryPermission()
            showKeyboard()
        }
    }

    private fun getGalleryPermission() {
        // api 34 이상인 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            selectImage()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            requestPermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun selectImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            getGalleryLauncher.launch("image/*")
        } else {
            getPhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun observePhotoUri() {
        viewModel.photoUri.flowWithLifecycle(viewLifeCycle).onEach { getUri ->
            getUri?.let { uri ->
                handleUploadImageClick(Uri.parse(uri))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleUploadImageClick(uri: Uri) = with(binding) {
        groupPostingImage.isVisible = true
        ivPostingPhoto.load(uri)
        initDeletePhotoBtnClickListener()
    }

    private fun initDeletePhotoBtnClickListener() = with(binding) {
        ivPostingPhotoDelete.setOnClickListener {
            viewModel.setPhotoUri(null)
            ivPostingPhoto.load(null)
            groupPostingImage.isGone = true
        }
    }

    @SuppressLint("ServiceCast")
    private fun showKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            binding.etPostingContent,
            InputMethodManager.SHOW_IMPLICIT,
        )
        binding.etPostingContent.requestFocus()
    }

    private fun initBackBtnClickListener() {
        binding.ivPostingAppbarBack.setOnClickListener {
            PostingExitDialogFragment().show(childFragmentManager, POSTING_EXIT_DIALOG)
        }
    }

    private fun initEditTextBtn() {
        binding.run {
            etPostingContent.doAfterTextChanged {
                totalContentLength = etPostingContent.text.length
                handleUploadProgressAndBtn(totalContentLength)
            }
        }
    }

    private fun handleUploadProgressAndBtn(totalContentLength: Int) {
        when {
            (totalContentLength in POSTING_MIN..POSTING_MAX) -> {
                updateProgress(
                    com.teamwable.ui.R.color.gray_600,
                    com.teamwable.ui.R.color.white,
                ) {
                    initUploadingActivateBtnClickListener()
                }
            }

            totalContentLength >= POSTING_MAX + 1 -> {
                updateProgress(
                    com.teamwable.ui.R.color.error,
                    com.teamwable.ui.R.color.gray_600,
                ) {}
            }

            else -> {
                updateProgress(
                    com.teamwable.ui.R.color.gray_600,
                    com.teamwable.ui.R.color.gray_600,
                ) {}
            }
        }
        return updateTextCount(totalContentLength)
    }

    private fun initUploadingActivateBtnClickListener() {
        binding.btnPostingUpload.setOnClickListener {
            // Todo : 나중에 추가해야 함
        }
    }

    private fun updateProgress(
        countTextColorResId: Int,
        textColorResId: Int,
        clickListener: () -> Unit,
    ) {
        binding.tvPostingProgress.setTextColor(colorOf(countTextColorResId))
        setUploadingBtnColor(textColorResId)
        clickListener.invoke()
    }

    private fun setUploadingBtnColor(
        textColorResId: Int,
    ) {
        binding.btnPostingUpload.setTextColor(
            colorOf(textColorResId),
        )
    }

    private fun updateTextCount(totalCommentLength: Int) {
        binding.tvPostingProgress.text = getString(R.string.tv_posting_progress_count, totalCommentLength)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setPhotoUri(null)
    }

    companion object {
        const val POSTING_MIN = 1
        const val POSTING_MAX = 499
    }
}
