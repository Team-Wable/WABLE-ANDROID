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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.extension.modifier.wableVerticalGradientBackground
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.R
import com.teamwable.quiz.component.QuizResultType
import com.teamwable.quiz.component.QuizStatBox
import com.teamwable.quiz.component.QuizStatType

@Composable
fun QuizResultRoute(
    navigateToMain: () -> Unit,
) {
    QuizResultScreen(
        onXpClick = navigateToMain,
    )
}

@Composable
fun QuizResultScreen(
    type: QuizResultType = QuizResultType.FAIL,
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
            painter = painterResource(id = type.image),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = type.title),
            style = WableTheme.typography.head00,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = type.description),
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
                value = "8",
            )
            QuizStatBox(
                title = stringResource(id = QuizStatType.RANK.title),
                titleColor = QuizStatType.RANK.titleColor,
                value = "16",
            )
            QuizStatBox(
                title = stringResource(id = QuizStatType.SPEED.title),
                titleColor = QuizStatType.SPEED.titleColor,
                value = "20",
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
        QuizResultScreen(
            type = QuizResultType.SUCCESS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizResultScreenFailPreview() {
    WableTheme {
        QuizResultScreen(
            type = QuizResultType.FAIL,
        )
    }
}
