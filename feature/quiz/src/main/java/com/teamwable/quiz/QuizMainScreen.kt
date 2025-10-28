package com.teamwable.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamwable.designsystem.component.button.BoardRequestButton
import com.teamwable.designsystem.component.dot.WableDot
import com.teamwable.designsystem.extension.preview.DevicePreviews
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun QuizMainRoute(
    viewModel: QuizMainViewModel = hiltViewModel(),
    onBtnClick: () -> Unit,
) {
    val time by viewModel.remainingTime.collectAsStateWithLifecycle()

    QuizMainScreen(
        time = time,
        onBtnClick = onBtnClick,
    )
}

@Composable
fun QuizMainScreen(
    time: String = "00:00",
    onBtnClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WableTheme.colors.gray100),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(42.dp))

            Text(
                text = stringResource(R.string.str_quiz_main_title),
                style = WableTheme.typography.head00,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(26.dp))

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
                    text = time,
                    style = WableTheme.typography.priceDown,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y = (-6).dp),
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            repeat(2) {
                WableDot()
                Spacer(modifier = Modifier.height(16.dp))
            }
            Box {
                Image(
                    painter = painterResource(id = R.drawable.img_quiz_before),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(),
                )
                BoardRequestButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    textWhite = stringResource(R.string.str_quiz_main_btn_white),
                    textSky = stringResource(R.string.str_quiz_main_btn_sky),
                    onClick = onBtnClick,
                )
            }
        }
    }
}

@Composable
@DevicePreviews
private fun QuizMainScreenPreview() {
    WableTheme {
        QuizMainScreen()
    }
}
