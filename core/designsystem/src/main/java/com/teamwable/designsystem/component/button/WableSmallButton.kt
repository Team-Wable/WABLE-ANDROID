package com.teamwable.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.extension.modifier.noRippleDebounceClickable
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableSmallButton(
    text: String,
    enabled: Boolean = true,
    textStyle: TextStyle = WableTheme.typography.body03,
    radius: Dp = 8.dp,
    minHeight: Dp = 0.dp,
    paddingVertical: Dp = 13.dp,
    paddingHorizontal: Dp = 16.dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(minHeight)
            .run {
                if (enabled) noRippleDebounceClickable(onClick = onClick)
                else this
            }
            .clip(RoundedCornerShape(radius))
            .background(if (enabled) WableTheme.colors.gray900 else WableTheme.colors.gray200)
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (enabled) WableTheme.colors.gray100 else WableTheme.colors.gray600,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun WableSmallButtonPreview() {
    WableTheme {
        Column {
            WableSmallButton(text = "ㅎㅇㅎㅇ", onClick = {}, enabled = true)
            WableSmallButton(text = "zzzzzzzzzzzz", onClick = {}, enabled = false)
        }
    }
}
