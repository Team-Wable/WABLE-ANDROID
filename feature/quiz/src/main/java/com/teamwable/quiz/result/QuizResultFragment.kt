package com.teamwable.quiz.result

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.databinding.FragmentQuizResultBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.type.SnackbarType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizResultFragment :
    BindingFragment<FragmentQuizResultBinding>(FragmentQuizResultBinding::inflate) {
    private val args: QuizResultFragmentArgs by navArgs()

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
                        resultModel = args.quizResultModel,
                        onShowErrorSnackBar = ::showSnackBar,
                    )
                }
            }
        }
    }

    private fun navigateToMain() {
        val action = QuizResultFragmentDirections.actionQuizResultFragmentToNavigationQuizMain()
        findNavController().navigate(action)
    }

    private fun showSnackBar(throwable: Throwable) {
        Snackbar.make(binding.root, SnackbarType.ERROR, throwable).show()
    }
}
