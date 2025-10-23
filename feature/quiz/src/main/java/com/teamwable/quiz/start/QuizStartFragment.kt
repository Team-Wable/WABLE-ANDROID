package com.teamwable.quiz.start

import androidx.compose.ui.platform.ViewCompositionStrategy
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.databinding.FragmentQuizStartBinding
import com.teamwable.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizStartFragment :
    BindingFragment<FragmentQuizStartBinding>(FragmentQuizStartBinding::inflate) {
    override fun initView() {
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    QuizStartScreen()
                }
            }
        }
    }
}
