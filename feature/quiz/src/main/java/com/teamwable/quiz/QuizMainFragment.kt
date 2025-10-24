package com.teamwable.quiz

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.teamwable.common.util.LinkStorage
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.databinding.FragmentQuizMainBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.openUri
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class QuizMainFragment : BindingFragment<FragmentQuizMainBinding>(FragmentQuizMainBinding::inflate) {
    private val viewModel: QuizMainViewModel by viewModels()

    override fun initView() {
        viewModel.isQuizCompleted.flowWithLifecycle(viewLifeCycle).onEach { completed ->
            val isCompleted = viewModel.isQuizCompleted.first { it != null }
            if (isCompleted == true) initComposeView()
            else navigateToStart()
        }.launchIn(viewLifeCycleScope)
    }

    private fun initComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WableTheme {
                    QuizMainRoute(
                        onBtnClick = ::navigateToGoogleForm,
                    )
                }
            }
        }
    }

    private fun navigateToStart() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.graph_quiz, true)
            .build()

        findNavController().navigate(R.id.navigation_quiz_start, null, navOptions)
    }

    private fun navigateToGoogleForm() {
        openUri(LinkStorage.GOOGLE_FORM_LINK)
    }
}
