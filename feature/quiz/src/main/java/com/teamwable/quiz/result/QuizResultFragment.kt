package com.teamwable.quiz.result

import androidx.compose.ui.platform.ViewCompositionStrategy
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.databinding.FragmentQuizResultBinding
import com.teamwable.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizResultFragment :
    BindingFragment<FragmentQuizResultBinding>(FragmentQuizResultBinding::inflate) {
    override fun initView() {
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    QuizResultScreen()
                }
            }
        }
    }
}
