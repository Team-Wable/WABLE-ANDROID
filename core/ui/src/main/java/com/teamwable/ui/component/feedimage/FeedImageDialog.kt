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
     * Initializes the dialog and retrieves the image URL from the fragment arguments.
     *
     * @param savedInstanceState The previously saved state of the fragment, if any.
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
     * Initializes the dialog's UI components and sets up event listeners.
     *
     * Loads the feed image, configures cancel and save button actions, and starts collecting ViewModel side effects.
     */
    override fun initView() {
        binding.ivFeedImg.load(imgUrl)
        initCancelBtnClickListener()
        initSaveBtnClickListener()
        collect()
    }

    /**
     * Sets up the cancel button to close the dialog and return to the previous screen when clicked.
     */
    private fun initCancelBtnClickListener() {
        binding.btnFeedImgCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Sets up the save button to trigger image saving when clicked, preventing duplicate clicks.
     */
    private fun initSaveBtnClickListener() {
        binding.tvFeedSave.setOnDuplicateBlockClick { viewModel.saveImage(imgUrl) }
    }

    /**
     * Collects side effects from the ViewModel and displays a Snackbar when instructed.
     *
     * Launches a coroutine to observe the ViewModel's sideEffect flow and shows a Snackbar on the dialog's root view when a ShowSnackBar event is received.
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
         * Displays the FeedImageDialog for the specified image URL using navigation.
         *
         * @param context The context used for navigation.
         * @param navController The NavController to perform navigation.
         * @param url The image URL to display in the dialog.
         * @return The FeedImageDialog instance that was shown.
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
