package com.teamwable.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.R
import com.teamwable.designsystem.extension.modifier.noRippleDebounceClickable
import com.teamwable.designsystem.theme.WableTheme

/**
 * [WableButton]은 Wable 디자인 시스템의 버튼을 나타내는 컴포저블 함수입니다.
 *
 * 이 함수는 버튼의 텍스트, 스타일, 활성화 상태 등을 설정할 수 있으며, 클릭 이벤트를 처리할 수 있습니다.
 * - [modifier] 버튼의 Modifier를 설정합니다.
 * - [text] 버튼에 표시될 텍스트입니다.
 * - [enabled] 버튼의 활성화 상태를 설정합니다. 기본값은 true입니다.
 * - [buttonStyle] 버튼의 스타일을 설정합니다. 기본값은 `defaultBigButtonStyle()`입니다.
 * - [onClick] 버튼 클릭 시 호출될 콜백 함수입니다.
 */
@Composable
fun WableButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    buttonStyle: BigButtonStyle = BigButtonDefaults.defaultBigButtonStyle(),
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(buttonStyle.aspectRatio)
            .run {
                if (enabled) noRippleDebounceClickable(onClick = onClick)
                else this
            }
            .clip(RoundedCornerShape(buttonStyle.radius))
            .background(buttonStyle.backgroundColor(enabled)),
    ) {
        Text(
            text = text,
            style = buttonStyle.textStyle,
            color = buttonStyle.textColor(enabled),
        )
    }
}

/**
 * [ButtonDefault]는 모든 버튼 스타일이 공통적으로 가져야 할 속성을 정의하는 인터페이스입니다.
 *
 * 추상체는 기본적인 버튼 스타일을 제공하며, 추가적인 속성이 필요할 경우 확장하여 사용 가능합니다.
 *
 * @property radius 버튼의 모서리 둥글기 (Corner Radius)
 * @property textStyle 버튼의 텍스트 스타일 (Typography)
 * @property backgroundColor 버튼의 활성화 상태에 따라 변하는 배경 색상
 * @property textColor 버튼의 활성화 상태에 따라 변하는 텍스트 색상
 */
interface ButtonDefault {
    val radius: Dp
    val textStyle: TextStyle
    val backgroundColor: @Composable (Boolean) -> Color
    val textColor: @Composable (Boolean) -> Color
}

/**
 * [BigButtonStyle]은 Wable 디자인 시스템의 버튼 스타일을 정의하는 데이터 클래스입니다.
 *
 * @property radius 버튼의 모서리 둥글기 (Corner Radius)
 * @property textStyle 버튼의 텍스트 스타일 (Typography)
 * @property backgroundColor 버튼의 활성화 상태에 따라 변하는 배경 색상
 * @property textColor 버튼의 활성화 상태에 따라 변하는 텍스트 색상
 * @property aspectRatio 버튼의 가로 세로 비율 (예: 1.5f → 가로가 세로의 1.5배)
 */
@Stable
data class BigButtonStyle(
    override val radius: Dp,
    override val textStyle: TextStyle,
    override val backgroundColor: @Composable (Boolean) -> Color,
    override val textColor: @Composable (Boolean) -> Color,
    val aspectRatio: Float = 5.86f,
) : ButtonDefault

object BigButtonDefaults {
    /**
     * 1.[defaultBigButtonStyle] 함수는
     * 기본 big버튼(가로 세로 비율로 크기를 결정) 스타일을 반환하는 컴포저블 함수입니다.
     *
     * 이 함수는 [BigButtonStyle] 인스턴스를 생성하여 반환하며, 버튼의 반경은
     * [R.dimen.radius_12]에서 가져온 값을 사용합니다. 또한, 버튼의 텍스트 스타일은
     * [WableTheme.typography.head02]로 설정되며, 활성화 상태에 따라 배경 및 텍스트 색상이 달라집니다.
     *
     * @return Wable 디자인 시스템의 빅 버튼 스타일을 나타내는 [BigButtonStyle] 인스턴스.
     *
     * 2.버튼 스타일을 UI단에서 직접 사용자 정의하려면 `copy()`를 사용하여 기존 스타일을 수정하세요.
     * 예시:
     * buttonStyle = BigButtonDefaults.defaultBigButtonStyle().copy(
     *     backgroundColor = { WableTheme.colors.red200 }
     * )
     *
     * 3.새로운 버튼 디자인 시스템을 추가하려면 아래의 [blackBigButtonStyle] 컴포저블 메소드를 참고해주세요.
     */

    @Composable
    @ReadOnlyComposable
    fun defaultBigButtonStyle() = BigButtonStyle(
        radius = dimensionResource(id = R.dimen.radius_12),
        textStyle = WableTheme.typography.head02,
        backgroundColor = { enabled -> if (enabled) WableTheme.colors.purple50 else WableTheme.colors.gray200 },
        textColor = { enabled -> if (enabled) WableTheme.colors.white else WableTheme.colors.gray600 },
    )

    /**
     * 기본 빅 버튼 스타일을 기반으로 배경색을 검정색으로 변경한 스타일을 반환하는 함수입니다.
     */
    @Composable
    @ReadOnlyComposable
    fun blackBigButtonStyle() = defaultBigButtonStyle().copy(
        backgroundColor = { WableTheme.colors.black },
    )

    @Composable
    @ReadOnlyComposable
    fun dialogButtonStyle() = defaultBigButtonStyle().copy(
        aspectRatio = 5.5f,
        textStyle = WableTheme.typography.body01,
    )
}

@Preview
@Composable
private fun WableButtonPreview() {
    WableTheme {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            WableButton(text = "ㅎㅇㅎㅇ", onClick = {}, enabled = true)
            WableButton(text = "zzzzzzzzzzzz", onClick = {}, enabled = false)
            WableButton(text = "zzzzzzzzzzzz", onClick = {}, buttonStyle = BigButtonDefaults.blackBigButtonStyle())
        }
    }
}
