package com.teamwable.quiz

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.databinding.FragmentQuizMainBinding
import com.teamwable.ui.base.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizMainFragment : BindingFragment<FragmentQuizMainBinding>(FragmentQuizMainBinding::inflate) {
    override fun initView() {
        // if (checkQuizStatus())
        initComposeView()
        // navigateToStart()
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    QuizMainScreen()
                }
            }
        }
    }

    private fun checkQuizStatus(): Boolean = false // 예시

    private fun navigateToStart() {
        findNavController().navigate(R.id.navigation_quiz_start)
    }
}
