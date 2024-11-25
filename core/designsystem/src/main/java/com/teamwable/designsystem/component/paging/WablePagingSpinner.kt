package com.teamwable.designsystem.component.paging

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WablePagingSpinner(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SpinnerAnimation")
    val animatedRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "AnimatedRotation",
    )

    val rotation by remember {
        derivedStateOf { animatedRotation.value }
    }

    Box(
        modifier = modifier
            .graphicsLayer(rotationZ = rotation),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            imageVector = toImageVector(com.teamwable.common.R.drawable.iv_share_loading_spinner),
            contentDescription = "Rotating Icon",
            modifier = Modifier.size(27.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WableRotatingAnimationPreview() {
    WableTheme {
        WablePagingSpinner()
    }
}
