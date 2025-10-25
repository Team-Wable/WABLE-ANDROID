package com.teamwable.quiz.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.image.WableGlideImage
import com.teamwable.designsystem.component.topbar.WableAppBar
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.quiz.QuizResultModel
import com.teamwable.quiz.R
import com.teamwable.quiz.component.OXType
import com.teamwable.quiz.component.QuizOXButton
import com.teamwable.quiz.start.model.QuizStartIntent
import com.teamwable.quiz.start.model.QuizStartSideEffect
import com.teamwable.quiz.start.model.QuizStartState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuizStartRoute(
    viewModel: QuizStartViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToResult: (QuizResultModel) -> Unit,
    onShowErrorSnackBar: (Throwable) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    is QuizStartSideEffect.NavigateToResult -> navigateToResult(sideEffect.model)
                    QuizStartSideEffect.NavigateUp -> navigateUp()
                    is QuizStartSideEffect.ShowSnackBar -> onShowErrorSnackBar(sideEffect.message)
                }
            }
    }

    QuizStartScreen(
        state = state,
        onAppBarClick = { viewModel.onIntent(QuizStartIntent.ClickAppBarBack) },
        onSubmitClick = { viewModel.onIntent(QuizStartIntent.ClickSubmitBtn) },
        onOxBtnClick = { type -> viewModel.onIntent(QuizStartIntent.ClickOXButton(type)) },
    )
}

@Composable
fun QuizStartScreen(
    state: QuizStartState = QuizStartState(),
    onAppBarClick: () -> Unit,
    onSubmitClick: () -> Unit = {},
    onOxBtnClick: (OXType) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WableAppBar(
            visibility = true,
            canNavigateBack = true,
            canClose = false,
            navigateUp = onAppBarClick,
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.str_quiz_start_title),
            style = WableTheme.typography.head00,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        WableGlideImage(
            imageUrl = state.quizModel.quizImage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(168.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = state.quizModel.quizText,
            style = WableTheme.typography.head01,
            textAlign = TextAlign.Center,
            color = WableTheme.colors.gray800,
            maxLines = 4,
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            QuizOXButton(
                isSelected = state.oxType == OXType.O,
                type = OXType.O,
                onClick = { onOxBtnClick(OXType.O) },
                modifier = Modifier.weight(1f),
            )
            QuizOXButton(
                isSelected = state.oxType == OXType.X,
                type = OXType.X,
                onClick = { onOxBtnClick(OXType.X) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        WableButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.btn_quiz_start_submit),
            enabled = state.enabled,
            onClick = onSubmitClick,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
@Preview(showBackground = true, heightDp = 780)
private fun QuizMainScreenPreview() {
    WableTheme {
        QuizStartScreen(
            onAppBarClick = {},
            onOxBtnClick = {},
        )
    }
}
