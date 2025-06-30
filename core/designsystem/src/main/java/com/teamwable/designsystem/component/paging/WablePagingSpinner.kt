package com.teamwable.designsystem.component.paging

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.PositionalThreshold
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

/**
 * Wable 앱에서 사용하는 커스텀 Pull-to-Refresh 인디케이터 컴포넌트입니다.
 *
 * 사용자가 리스트를 아래로 당길 때 나타나는 인디케이터로,
 * 새로고침 진행 중에는 스피너 애니메이션을 보여주고,
 * 대기 상태에서는 이미지에 거리 비례 스케일 애니메이션을 적용합니다.
 *
 * @param state Pull-to-Refresh 동작을 제어하는 상태 객체입니다.
 * @param isRefreshing 현재 새로고침 중인지를 나타내는 플래그입니다.
 * @param modifier 외부에서 전달받는 Modifier로 크기나 정렬 등을 조절할 수 있습니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WableCustomRefreshIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.pullToRefreshIndicator(
            state = state,
            isRefreshing = isRefreshing,
            containerColor = Color.White,
            elevation = 0.dp,
            threshold = PositionalThreshold,
        ),
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(
            targetState = isRefreshing,
            animationSpec = tween(durationMillis = 250),
            modifier = Modifier.align(Alignment.Center), label = "",
        ) { refreshing ->
            if (refreshing) {
                WablePagingSpinner()
            } else {
                val distanceFraction = state.distanceFraction.coerceIn(0f, 1f)

                Image(
                    imageVector = toImageVector(com.teamwable.common.R.drawable.iv_share_loading_spinner),
                    contentDescription = "Pull to refresh",
                    modifier = Modifier
                        .size(27.dp)
                        .graphicsLayer {
                            scaleX = distanceFraction
                            scaleY = distanceFraction
                            alpha = distanceFraction
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WableRotatingAnimationPreview() {
    WableTheme {
        WablePagingSpinner()
    }
}
