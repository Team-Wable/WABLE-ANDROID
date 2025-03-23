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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.extension.modifier.noRippleThrottleClickable
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.SmallButtonDefaults
import com.teamwable.designsystem.type.SmallButtonStyle

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
