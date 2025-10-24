package com.teamwable.quiz.start

import android.os.Bundle
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.R
import com.teamwable.quiz.databinding.FragmentQuizStartBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.type.SnackbarType
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
                    QuizStartRoute(
                        navigateUp = { findNavController().popBackStack() },
                        navigateToResult = ::navigateToResult,
                        onShowErrorSnackBar = ::showSnackBar,
                    )
                }
            }
        }
    }

    private fun navigateToResult(time: Int) {
        val bundle = Bundle().apply { putInt("time", time) }
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.navigation_quiz_start, true)
            .build()
        findNavController().navigate(R.id.navigation_quiz_result, bundle, navOptions)
    }

    private fun showSnackBar(throwable: Throwable) {
        Snackbar.make(binding.root, SnackbarType.ERROR, throwable).show()
    }
}
