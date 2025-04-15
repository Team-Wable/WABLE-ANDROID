package com.teamwable.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

/**
 * [FloatingButtonStyle]은 [WableFloatingButton]에 전달되는 스타일 정보를 담는 데이터 클래스입니다.
 *
 * - `size`: 버튼의 전체 크기 (가로 = 세로)
 * - `shape`: 버튼의 외곽 모양 (기본값은 원형 [CircleShape])
 * - `backgroundBrush`: 그라데이션을 위한 브러시, 없으면 단색 배경 사용
 * - `backgroundColor`: 단색 배경 컬러, `backgroundBrush`가 없을 때 적용됨
 * - `iconTint`: 아이콘의 색상
 * - `elevation`: 그림자 깊이 (DP 단위)
 */

@Stable
data class FloatingButtonStyle(
    val size: Dp = 56.dp,
    val shape: Shape = CircleShape,
    val backgroundBrush: Brush? = null,
    val backgroundColor: Color = Color.Transparent,
    val iconTint: Color = Color.White,
    val elevation: Dp = 4.dp,
)

/**
 * [FloatingButtonDefaults]는 Wable에서 사용하는 FAB 스타일을 미리 정의한 객체입니다.
 *
 * 디자인 시스템에서 통일된 버튼 스타일을 제공하기 위해 사용됩니다.
 */
object FloatingButtonDefaults {
    /**
     * 그라데이션 배경이 적용된 기본 스타일입니다.
     *
     * 보통 '추가(+)' 버튼 등의 주요 액션 FAB에 사용됩니다.
     *
     * @return [FloatingButtonStyle] 그라데이션 배경, 흰색 아이콘, 투명 배경 색
     */
    @Composable
    @ReadOnlyComposable
    fun gradientStyle(): FloatingButtonStyle = FloatingButtonStyle(
        backgroundBrush = Brush.verticalGradient(
            listOf(
                Color(0xFF04E9D0),
                Color(0xFF54AAFA),
                Color(0xFFB649FF),
            ),
        ),
        backgroundColor = Color.Transparent,
        iconTint = WableTheme.colors.white,
    )

    /**
     * 회색 단색 배경이 적용된 중립적인 스타일입니다.
     *
     * 보통 '닫기(X)', '취소'와 같은 보조적 FAB에 사용됩니다.
     *
     * @return [FloatingButtonStyle] 회색 배경, 어두운 아이콘 색상
     */
    @Composable
    @ReadOnlyComposable
    fun grayStyle(): FloatingButtonStyle = FloatingButtonStyle(
        backgroundBrush = null,
        backgroundColor = WableTheme.colors.gray200,
        iconTint = WableTheme.colors.gray800,
    )
}

/**
 * 디자인 시스템에서 사용하는 공통 Floating Action Button입니다.
 *
 * 스타일은 [FloatingButtonStyle]을 통해 주입받으며, 아이콘과 클릭 이벤트를 받아 구성됩니다.
 * 버튼은 내부적으로 [Surface]와 [IconButton]을 감싸는 구조로 되어 있습니다.
 *
 * @param modifier 외부에서 전달 가능한 Modifier
 * @param onClick FAB 클릭 시 실행할 콜백
 * @param icon 버튼 내부에 표시될 아이콘 (예: painterResource(R.drawable.ic_add))
 * @param contentDescription 접근성 지원을 위한 설명 텍스트
 * @param style FAB의 전체 외형 및 스타일 지정 ([FloatingButtonDefaults] 참조)
 *
 * ### 사용 예시
 * ```kotlin
 * WableFloatingButton(
 *     onClick = { /* TODO */ },
 *     icon = painterResource(id = R.drawable.ic_home_posting),
 *     contentDescription = "추가",
 *     style = FloatingButtonDefaults.gradientStyle()
 * )
 *
 * ```
 * ### 커스텀 예시
 * ```kotlin
 * val largeBlueFabStyle = FloatingButtonDefaults.grayStyle().copy(
 *     backgroundColor = Color.Blue,
 *     iconTint = Color.White,
 *     size = 72.dp
 * )
 *
 * WableFloatingButton(
 *     onClick = { /* TODO */ },
 *     icon = painterResource(id = R.drawable.ic_share),
 *     contentDescription = "공유",
 *     style = largeBlueFabStyle
 * )
 */
@Composable
fun WableFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: Painter,
    contentDescription: String? = null,
    style: FloatingButtonStyle,
) {
    Surface(
        modifier = modifier.size(style.size),
        shape = style.shape,
        shadowElevation = style.elevation,
        color = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .background(
                    shape = style.shape,
                    brush = style.backgroundBrush ?: Brush.verticalGradient(
                        listOf(style.backgroundColor, style.backgroundColor),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription,
                    tint = style.iconTint,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WableFloatingButtonPreview() {
    WableTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            WableFloatingButton(
                onClick = { },
                icon = painterResource(id = com.teamwable.common.R.drawable.ic_home_posting),
                style = FloatingButtonDefaults.gradientStyle(),
            )

            WableFloatingButton(
                onClick = { },
                icon = painterResource(id = com.teamwable.common.R.drawable.ic_share_cancel_btn),
                style = FloatingButtonDefaults.grayStyle(),
            )
        }
    }
}
