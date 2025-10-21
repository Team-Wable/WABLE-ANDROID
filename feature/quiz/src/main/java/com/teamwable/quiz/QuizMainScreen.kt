package com.teamwable.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamwable.designsystem.component.button.BoardRequestButton
import com.teamwable.designsystem.component.dot.WableDot
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun QuizMainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WableTheme.colors.gray100),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(54.dp))

        Text(
            text = stringResource(R.string.str_quiz_main_title),
            style = WableTheme.typography.head00,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_quiz_time),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Text(
                text = "99:99",
                textAlign = TextAlign.Center,
                fontSize = 88.sp,
            )
        }
        repeat(2) {
            Spacer(modifier = Modifier.height(16.dp))
            WableDot()
        }
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_quiz_before),
                contentDescription = null,
                modifier = Modifier.aspectRatio(360 / 196f),
            )
            BoardRequestButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                onClick = {},
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun QuizMainScreenPreview() {
    WableTheme {
        QuizMainScreen()
    }
}
