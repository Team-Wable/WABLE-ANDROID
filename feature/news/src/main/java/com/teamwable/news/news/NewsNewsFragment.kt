package com.teamwable.news.news

import androidx.compose.ui.platform.ViewCompositionStrategy
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.news.databinding.FragmentNewsNewsBinding
import com.teamwable.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsNewsFragment : BindingFragment<FragmentNewsNewsBinding>(FragmentNewsNewsBinding::inflate) {
    override fun initView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                }
            }
        }
    }
}
