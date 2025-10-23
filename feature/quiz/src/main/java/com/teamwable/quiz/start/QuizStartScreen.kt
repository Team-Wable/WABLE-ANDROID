package com.teamwable.quiz.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.image.WableGlideImage
import com.teamwable.designsystem.component.topbar.WableAppBar
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.R
import com.teamwable.quiz.component.OXType
import com.teamwable.quiz.component.QuizOXButton

@Composable
fun QuizStartScreen(
    type: OXType? = null,
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
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.str_quiz_start_title),
            style = WableTheme.typography.head00,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        WableGlideImage(
            imageUrl = "",
            modifier = Modifier.heightIn(168.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "이 룬은 칼날비라는 룬으로\n" +
                "적에게 기본 공격을 3번 가하면\n" +
                "일정 시간 동안 공격 속도가\n" +
                "크게 증가하는 룬이다.",
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
                isSelected = type == OXType.O,
                type = OXType.O,
                onClick = { if (type == OXType.O) null else OXType.O },
                modifier = Modifier.weight(1f),
            )
            QuizOXButton(
                isSelected = type == OXType.X,
                type = OXType.X,
                onClick = { if (type == OXType.X) null else OXType.X },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        WableButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.btn_quiz_start_submit),
            onClick = {},
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
@Preview(showBackground = true, heightDp = 780)
private fun QuizMainScreenPreview() {
    WableTheme {
        QuizStartScreen()
    }
}
