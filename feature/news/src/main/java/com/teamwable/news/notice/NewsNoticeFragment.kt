package com.teamwable.news.notice

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.NewsFragmentDirections
import com.teamwable.news.databinding.FragmentNewsNoticeBinding
import com.teamwable.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsNoticeFragment : BindingFragment<FragmentNewsNoticeBinding>(FragmentNewsNoticeBinding::inflate) {
    override fun initView() {
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeNewsNotice.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    NewsNoticeRoute(navigateToDetail = ::navigateToDetail)
                }
            }
        }
    }

    private fun navigateToDetail(notice: NewsInfoModel) {
        val parentNavController = requireParentFragment().findNavController()
        val action = NewsFragmentDirections.actionNavigationNewsToNavigationNewsDetail(notice)
        parentNavController.navigate(action)
    }
}
