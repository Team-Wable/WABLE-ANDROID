package com.teamwable.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.extension.modifier.noRippleThrottleClickable
import com.teamwable.designsystem.theme.WableTheme

/**
 * [SmallButtonStyle]은 Wable 디자인 시스템에서 사용되는 작은 버튼의 스타일을 정의하는 데이터 클래스입니다.
 *
 * 버튼의 모서리 둥글기, 텍스트 스타일, 배경색, 텍스트 색상, 패딩 등을 설정할 수 있습니다. 이 스타일을 바탕으로
 * 다양한 크기와 모양의 작은 버튼을 생성할 수 있습니다.
 *
 * @param radius 버튼의 모서리 둥글기 (Corner Radius)
 * @param textStyle 버튼의 텍스트 스타일 (Typography)
 * @param backgroundColor 버튼의 활성화 상태에 따라 변하는 배경 색상
 * @param textColor 버튼의 활성화 상태에 따라 변하는 텍스트 색상
 * @param minHeight 버튼의 최소 높이
 * @param paddingVertical 버튼의 세로 패딩
 * @param paddingHorizontal 버튼의 가로 패딩
 */
@Stable
data class SmallButtonStyle(
    override val radius: Dp = 8.dp,
    override val textStyle: TextStyle,
    override val backgroundColor: @Composable (Boolean) -> Color,
    override val textColor: @Composable (Boolean) -> Color,
    val minHeight: Dp = 0.dp,
    val paddingVertical: Dp = 13.dp,
    val paddingHorizontal: Dp = 16.dp,
) : ButtonDefault

/**
 * [SmallButtonDefaults]는 기본 작은 버튼 스타일을 제공하는 객체입니다.
 *
 * 기본 스타일인 `defaultSmallButtonStyle`과 미니 버튼 스타일인 `miniButtonStyle`을 제공합니다.
 */
object SmallButtonDefaults {
    /**
     * 기본 작은 버튼 스타일을 반환합니다.
     *
     * 기본적으로 회색 배경과 텍스트를 제공하며, 버튼이 비활성화되면 회색이 어두워집니다.
     *
     * ### 사용 예시
     * ```kotlin
     * val blackStyle = SmallButtonDefaults.defaultSmallButtonStyle()
     * ```
     * @return 기본 [SmallButtonStyle] 인스턴스
     */
    @Composable
    @ReadOnlyComposable
    fun defaultSmallButtonStyle() = SmallButtonStyle(
        radius = 8.dp,
        paddingVertical = 13.dp,
        paddingHorizontal = 16.dp,
        textStyle = WableTheme.typography.body03,
        backgroundColor = { enabled -> if (enabled) WableTheme.colors.gray900 else WableTheme.colors.gray200 },
        textColor = { enabled -> if (enabled) WableTheme.colors.gray100 else WableTheme.colors.gray600 },
    )

    /**
     * 미니 버튼 스타일을 반환합니다.
     *
     * 기본 작은 버튼 스타일을 바탕으로 패딩과 모서리 둥글기를 조정하고, 텍스트 색상과 배경 색상을 변경합니다.
     * ### 사용 예시
     * ```kotlin
     * val blackStyle = SmallButtonDefaults.miniButtonStyle()
     * ```
     * @return 미니 버튼 스타일을 위한 [SmallButtonStyle] 인스턴스
     */
    @Composable
    @ReadOnlyComposable
    fun miniButtonStyle() = defaultSmallButtonStyle().copy(
        paddingVertical = 6.dp,
        paddingHorizontal = 14.dp,
        radius = 20.dp,
        textColor = { WableTheme.colors.white },
        backgroundColor = { WableTheme.colors.gray900 },
    )
}

/**
 * [WableSmallButton]은 Wable 디자인 시스템에서 사용되는 작은 버튼 컴포저블 함수입니다.
 *
 * 버튼의 텍스트, 스타일, 활성화 상태 및 클릭 이벤트를 처리할 수 있습니다. 텍스트뿐만 아니라 아이콘을 추가하여
 * 이미지와 텍스트가 함께 있는 버튼을 만들 수 있습니다.
 *
 * @param modifier 버튼의 크기 및 레이아웃을 조정하는 Modifier입니다.
 * @param text 버튼에 표시될 텍스트입니다.
 * @param enabled 버튼의 활성화 상태를 설정합니다. 기본값은 `true`입니다.
 * @param buttonStyle 버튼의 스타일을 지정합니다. 기본값은 [SmallButtonDefaults.defaultSmallButtonStyle]입니다.
 * @param onClick 버튼 클릭 시 실행할 콜백 함수입니다.
 * @param imageContent 버튼에 추가할 이미지 콘텐츠를 설정하는 함수입니다. 기본값은 빈 함수입니다.
 */
@Composable
fun WableSmallButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    buttonStyle: SmallButtonStyle = SmallButtonDefaults.defaultSmallButtonStyle(),
    onClick: () -> Unit,
    imageContent: @Composable () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(buttonStyle.minHeight)
            .run {
                if (enabled) noRippleThrottleClickable(onClick = onClick)
                else this
            }
            .clip(RoundedCornerShape(buttonStyle.radius))
            .background(buttonStyle.backgroundColor(enabled))
            .padding(
                horizontal = buttonStyle.paddingHorizontal,
                vertical = buttonStyle.paddingVertical,
            ),
    ) {
        imageContent()
        Text(
            text = text,
            style = buttonStyle.textStyle,
            color = buttonStyle.textColor(enabled),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun WableSmallButtonPreview() {
    WableTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            WableSmallButton(
                text = "ㅎㅇㅎㅇ",
                onClick = {},
            )
            WableSmallButton(
                text = "zzzzzzzzzzzz",
                onClick = {},
                enabled = false,
            )
            WableSmallButton(
                buttonStyle = SmallButtonDefaults.miniButtonStyle(),
                imageContent = {
                    Image(
                        imageVector = toImageVector(id = com.teamwable.common.R.drawable.ic_community_check),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.padding(start = 2.dp))
                },
                text = "사전 신청하기",
                onClick = {},
            )
        }
    }
}
