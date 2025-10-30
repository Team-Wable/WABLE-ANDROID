package com.teamwable.designsystem.component.indicator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableLinearProgressBar(
    modifier: Modifier = Modifier,
    progressStyle: ProgressStyle = defaultProgressStyle(),
    progress: () -> Float = { 0f },
    content: @Composable () -> Unit = {},
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress(),
        animationSpec = tween(durationMillis = 500),
        label = "",
    )

    Column {
        content()
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(progressStyle.cornerRadius))
                .fillMaxWidth()
                .background(color = progressStyle.backgroundColor),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(progressStyle.cornerRadius))
                    .fillMaxWidth(animatedProgress)
                    .background(color = progressStyle.progressColor)
                    .height(progressStyle.height),
            )
        }
    }
}

@Stable
data class ProgressStyle(
    val progressColor: Color,
    val backgroundColor: Color,
    val cornerRadius: Dp = 8.dp,
    val height: Dp = 10.dp,
)

@Composable
@ReadOnlyComposable
fun defaultProgressStyle() = ProgressStyle(
    progressColor = WableTheme.colors.t50,
    backgroundColor = WableTheme.colors.gray200,
)

@Composable
@Preview(showBackground = true)
private fun WableLinearProgressBarPreview() {
    WableTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            WableLinearProgressBar(
                modifier = Modifier.fillMaxWidth(),
                progressStyle = defaultProgressStyle(),
                progress = { 0.7f },
            ) {
                Text("asdfdsfsf")
                Spacer(modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}
