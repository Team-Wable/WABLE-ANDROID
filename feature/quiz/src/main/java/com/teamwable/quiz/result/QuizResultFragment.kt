package com.teamwable.quiz.result

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
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
                    QuizResultRoute(
                        navigateToMain = ::navigateToMain,
                    )
                }
            }
        }
    }

    private fun navigateToMain() {
        val action = QuizResultFragmentDirections.actionQuizResultFragmentToNavigationQuizMain()
        findNavController().navigate(action)
    }
}
