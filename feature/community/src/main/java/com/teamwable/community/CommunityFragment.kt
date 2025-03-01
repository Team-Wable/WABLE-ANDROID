package com.teamwable.community

import androidx.compose.ui.platform.ViewCompositionStrategy
import com.teamwable.community.databinding.FragmentCommunityBinding
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.ui.base.BindingFragment

class CommunityFragment : BindingFragment<FragmentCommunityBinding>(FragmentCommunityBinding::inflate) {
    override fun initView() {
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                }
            }
        }
    }
}
