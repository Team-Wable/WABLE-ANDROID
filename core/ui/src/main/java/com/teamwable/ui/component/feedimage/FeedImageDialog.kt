package com.teamwable.ui.component.feedimage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.teamwable.ui.base.BindingDialogFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.databinding.DialogFeedImageBinding
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.dialogFragmentResize
import com.teamwable.ui.extensions.load
import com.teamwable.ui.extensions.setOnDuplicateBlockClick
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.util.Arg.FEED_IMAGE_URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedImageDialog : BindingDialogFragment<DialogFeedImageBinding>(DialogFeedImageBinding::inflate) {
    private lateinit var imgUrl: String
    private val viewModel: FeedImageViewModel by viewModels()

    /**
     * Initializes the dialog by retrieving the image URL from the fragment arguments.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imgUrl = arguments?.getString(FEED_IMAGE_URL).orEmpty()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0f, 0f)
    }

    /**
     * Initializes the dialog's view by loading the image, setting up button listeners, and starting side effect collection.
     */
    override fun initView() {
        binding.ivFeedImg.load(imgUrl)
        initCancelBtnClickListener()
        initSaveBtnClickListener()
        collect()
    }

    /**
     * Sets up the cancel button to close the dialog and navigate back when clicked.
     */
    private fun initCancelBtnClickListener() {
        binding.btnFeedImgCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Sets up the save button to trigger image saving, preventing duplicate clicks.
     */
    private fun initSaveBtnClickListener() {
        binding.tvFeedSave.setOnDuplicateBlockClick { viewModel.saveImage(imgUrl) }
    }

    /**
     * Collects side effects from the ViewModel and displays a snackbar when a ShowSnackBar side effect is received.
     */
    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.sideEffect.flowWithLifecycle(viewLifeCycle).collectLatest { sideEffect ->
                when (sideEffect) {
                    is FeedImageSideEffect.ShowSnackBar -> Snackbar.make(binding.root, sideEffect.type, sideEffect.throwable).show()
                }
            }
        }
    }

    companion object {
        /**
         * Displays the FeedImageDialog for the specified image URL using deep link navigation.
         *
         * @param context The context used for navigation.
         * @param navController The navigation controller to perform the dialog navigation.
         * @param url The URL of the image to display in the dialog.
         * @return The created FeedImageDialog instance.
         */
        fun show(
            context: Context,
            navController: NavController,
            url: String,
        ) = FeedImageDialog().apply {
            navigateToDialog(context, navController, url)
        }

        private fun navigateToDialog(context: Context, navController: NavController, url: String) =
            navController.deepLinkNavigateTo(context, DeepLinkDestination.FeedImageDialog, mapOf(FEED_IMAGE_URL to url))
    }
}
