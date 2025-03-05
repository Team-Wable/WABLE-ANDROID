package com.teamwable.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import java.util.Locale

/**
 * [WableButton]은 Wable 디자인 시스템의 버튼을 나타내는 컴포저블 함수입니다.
 *
 * 이 버튼은 텍스트, 스타일, 활성화 상태 등을 설정할 수 있으며, 클릭 이벤트를 처리할 수 있습니다.
 *
 * @param modifier 버튼의 크기 및 레이아웃을 조정하는 Modifier입니다.
 * @param text 버튼에 표시될 텍스트입니다.
 * @param enabled 버튼의 활성화 상태를 설정합니다. 기본값은 `true`입니다.
 * @param buttonStyle 버튼의 스타일을 지정합니다. 기본값은 [BigButtonDefaults.defaultBigButtonStyle] 입니다.
 * @param onClick 버튼 클릭 시 실행할 콜백 함수입니다.
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
 * [BigButtonStyle]은 Wable 디자인 시스템에서 사용되는 버튼 스타일을 정의하는 데이터 클래스입니다.
 *
 * 가로, 세로 비율을 지정할 수 있으며, 다양한 버튼 스타일을 생성하는 데 사용할 수 있습니다.
 *
 * @property radius 버튼의 모서리 둥글기 (Corner Radius)
 * @property textStyle 버튼의 텍스트 스타일
 * @property backgroundColor 버튼의 활성화 상태에 따른 배경 색상
 * @property textColor 버튼의 활성화 상태에 따른 텍스트 색상
 * @property width 버튼의 가로 길이 (선택적, 기본값: 0)
 * @property height 버튼의 세로 길이 (선택적, 기본값: 0)
 * @property aspectRatio 버튼의 가로 세로 비율 (가로 / 세로). 기본값은 `5.86f`
 */
@Stable
data class BigButtonStyle(
    override val radius: Dp,
    override val textStyle: TextStyle,
    override val backgroundColor: @Composable (Boolean) -> Color,
    override val textColor: @Composable (Boolean) -> Color,
    val width: Int = 0,
    val height: Int = 0,
) : ButtonDefault {
    val aspectRatio: Float
        get() = calculateAspectRatio(width, height)
}

private fun calculateAspectRatio(width: Int, height: Int): Float = String.format(
    locale = Locale.US,
    format = "%.2f",
    if (width > 0 && height > 0) width.toFloat() / height else 5.86f,
).toFloat()

object BigButtonDefaults {
    private const val DEFAULT_BUTTON_WIDTH = 328
    private const val DEFAULT_BUTTON_HEIGHT = 56
    private const val DIALOG_BUTTON_WIDTH_264 = 264
    private const val DIALOG_BUTTON_HEIGHT_48 = 48

    /**
     * 기본 Big 버튼 스타일을 반환합니다.
     *
     * @return 기본 [BigButtonStyle] 인스턴스.
     *
     * ### 사용 예시
     * ```kotlin
     * val buttonStyle = BigButtonDefaults.defaultBigButtonStyle()
     * ```
     *
     * 기본 스타일을 유지하면서 일부 속성만 직접 변경하고 싶다면 `copy()`를 사용할 수 있습니다.
     * ```kotlin
     * val customStyle = BigButtonDefaults.defaultBigButtonStyle().copy(
     *     backgroundColor = { WableTheme.colors.red200 }
     * )
     * ```
     */

    @Composable
    @ReadOnlyComposable
    fun defaultBigButtonStyle() = BigButtonStyle(
        radius = dimensionResource(id = R.dimen.radius_12),
        width = DEFAULT_BUTTON_WIDTH,
        height = DEFAULT_BUTTON_HEIGHT,
        textStyle = WableTheme.typography.head02,
        backgroundColor = { enabled -> if (enabled) WableTheme.colors.purple50 else WableTheme.colors.gray200 },
        textColor = { enabled -> if (enabled) WableTheme.colors.white else WableTheme.colors.gray600 },
    )

    /**
     * 기본 Big 버튼 스타일에서 **배경색을 검정색**으로 변경한 스타일을 반환합니다.
     *
     * @return 배경이 검정색인 [BigButtonStyle] 인스턴스.
     *
     * ### 사용 예시
     * ```kotlin
     * val blackStyle = BigButtonDefaults.blackBigButtonStyle()
     * ```
     */
    @Composable
    @ReadOnlyComposable
    fun blackBigButtonStyle() = defaultBigButtonStyle().copy(
        height = DIALOG_BUTTON_HEIGHT_48,
        textStyle = WableTheme.typography.body03,
        backgroundColor = { WableTheme.colors.black },
    )

    @Composable
    @ReadOnlyComposable
    fun dialogButtonStyle() = defaultBigButtonStyle().copy(
        width = DIALOG_BUTTON_WIDTH_264,
        height = DIALOG_BUTTON_HEIGHT_48,
        textStyle = WableTheme.typography.body01,
    )

    @Composable
    @ReadOnlyComposable
    fun dialogTwoButtonStyle() = dialogButtonStyle().copy(
        width = 128,
    )
}

@Composable
fun WableTwoButtons(
    modifier: Modifier = Modifier,
    cancelButtonText: String = "취소",
    buttonText: String = "신청하기",
    buttonStyle: BigButtonStyle = BigButtonDefaults.dialogTwoButtonStyle(),
    onCancel: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        WableButton(
            modifier = Modifier.weight(1f),
            text = cancelButtonText,
            buttonStyle = BigButtonDefaults.dialogTwoButtonStyle().copy(
                backgroundColor = { WableTheme.colors.gray200 },
                textColor = { WableTheme.colors.gray600 },
            ),
            onClick = onCancel,
        )
        Spacer(modifier = Modifier.width(8.dp))
        WableButton(
            modifier = Modifier.weight(1f),
            text = buttonText,
            buttonStyle = buttonStyle,
            onClick = onClick,
        )
    }
}

@Preview
@Composable
private fun WableButtonPreview() {
    WableTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            WableButton(text = "ㅎㅇㅎㅇ", onClick = {}, enabled = true)
            WableButton(text = "zzzzzzzzzzzz", onClick = {}, enabled = false)
            WableButton(
                text = "zzzzzzzzzzzz",
                onClick = {},
                buttonStyle = BigButtonDefaults.blackBigButtonStyle(),
            )
            WableTwoButtons()
        }
    }
}
