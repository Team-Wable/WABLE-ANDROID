package com.teamwable.news.notice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.news.NewsFragmentDirections
import com.teamwable.news.databinding.FragmentNewsNoticeBinding
import com.teamwable.news.model.NewsInfoModel
import com.teamwable.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsNoticeFragment : BindingFragment<FragmentNewsNoticeBinding>(FragmentNewsNoticeBinding::inflate) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        initComposeView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComposeView() {
        binding.composeNewsNotice.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    NewsNoticeRoute(navigateToDetail = { notice -> navigateToDetail(notice) })
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
