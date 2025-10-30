package com.teamwable.quiz.result

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.util.AmplitudeQuizTag.CLICK_GET_XP
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.extension.modifier.wableVerticalGradientBackground
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.quiz.QuizResultModel
import com.teamwable.quiz.R
import com.teamwable.quiz.component.QuizStatBox
import com.teamwable.quiz.component.QuizStatType
import com.teamwable.quiz.result.model.QuizResultIntent
import com.teamwable.quiz.result.model.QuizResultSideEffect
import com.teamwable.quiz.result.model.QuizResultState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuizResultRoute(
    viewModel: QuizResultViewModel = hiltViewModel(),
    resultModel: QuizResultModel = QuizResultModel(),
    navigateToMain: () -> Unit,
    onShowErrorSnackBar: (Throwable) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(QuizResultIntent.LoadInitialData(model = resultModel))
    }

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    QuizResultSideEffect.NavigateToMain -> navigateToMain()
                    is QuizResultSideEffect.ShowSnackBar -> onShowErrorSnackBar(sideEffect.message)
                }
            }
    }

    QuizResultScreen(
        state = state,
        onXpClick = {
            viewModel.onIntent(QuizResultIntent.ClickXPBtn)
            trackEvent(CLICK_GET_XP)
        },
    )
}

@Composable
fun QuizResultScreen(
    state: QuizResultState = QuizResultState(),
    onXpClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wableVerticalGradientBackground(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = state.resultType.image),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = state.resultType.title),
            style = WableTheme.typography.head00,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = state.resultType.description),
            style = WableTheme.typography.head01,
            color = WableTheme.colors.gray800,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 27.dp),
        ) {
            QuizStatBox(
                title = stringResource(id = QuizStatType.XP.title),
                titleColor = QuizStatType.XP.titleColor,
                value = state.xp.toString(),
            )
            QuizStatBox(
                title = stringResource(id = QuizStatType.RANK.title),
                titleColor = QuizStatType.RANK.titleColor,
                value = "${state.userPercent}%",
            )
            QuizStatBox(
                title = stringResource(id = QuizStatType.SPEED.title),
                titleColor = QuizStatType.SPEED.titleColor,
                value = state.scoreTime,
            )
        }

        Spacer(modifier = Modifier.height(41.dp))

        WableButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.str_quiz_result_xp),
            onClick = onXpClick,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizResultScreenSuccessPreview() {
    WableTheme {
        QuizResultScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizResultScreenFailPreview() {
    WableTheme {
        QuizResultScreen()
    }
}
