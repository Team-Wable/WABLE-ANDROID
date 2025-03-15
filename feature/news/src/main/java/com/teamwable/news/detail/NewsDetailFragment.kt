package com.teamwable.news.detail

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamwable.common.util.LinkStorage
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.NewsTabType
import com.teamwable.news.databinding.FragmentNewsDetailBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.FeedImageDialog
import com.teamwable.ui.extensions.openUri
import java.net.URLEncoder

class NewsDetailFragment : BindingFragment<FragmentNewsDetailBinding>(FragmentNewsDetailBinding::inflate) {
    private val args: NewsDetailFragmentArgs by navArgs()
    private val notice: NewsInfoModel by lazy { args.newsInfoModel }
    private val newsTabType: NewsTabType by lazy { args.newsTabType }

    override fun initView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    NewsDetailScreen(
                        newsInfoModel = notice,
                        type = newsTabType,
                        navigateToImageDetail = ::navigateToImageDetail,
                        navigateToBack = ::navigateToBack,
                        navigateToGoogleForm = ::navigateToGoogleForm,
                    )
                }
            }
        }
    }

    private fun navigateToImageDetail() {
        if (notice.newsImage.isNullOrBlank()) return
        val encodedUrl = URLEncoder.encode(notice.newsImage, "UTF-8")
        FeedImageDialog.show(requireContext(), findNavController(), encodedUrl)
    }

    private fun navigateToBack() {
        findNavController().popBackStack()
    }

    private fun navigateToGoogleForm() {
        openUri(LinkStorage.GOOGLE_FORM_LINK)
    }
}
