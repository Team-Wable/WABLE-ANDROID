package com.teamwable.ui.component

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.teamwable.ui.base.BindingDialogFragment
import com.teamwable.ui.databinding.DialogFeedImageBinding
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.dialogFragmentResize
import com.teamwable.ui.extensions.load
import com.teamwable.ui.util.Arg.FEED_IMAGE_URL

class FeedImageDialog() : BindingDialogFragment<DialogFeedImageBinding>(DialogFeedImageBinding::inflate) {
    private lateinit var imgUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imgUrl = arguments?.getString(FEED_IMAGE_URL).orEmpty()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0f, 0f)
    }

    override fun initView() {
        binding.ivFeedImg.load(imgUrl)
        initCancelBtnClickListener()
    }

    private fun initCancelBtnClickListener() {
        binding.btnFeedImgCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
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
