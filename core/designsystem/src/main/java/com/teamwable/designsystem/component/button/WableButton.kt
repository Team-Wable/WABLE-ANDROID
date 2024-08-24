package com.teamwable.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.R
import com.teamwable.designsystem.extension.modifier.noRippleDebounceClickable
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableButton(
    text: String,
    enabled: Boolean = true,
    textStyle: TextStyle = WableTheme.typography.head02,
    paddingTop: Dp = 15.dp,
    paddingBottom: Dp = 12.dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .run {
                if (enabled) noRippleDebounceClickable(onClick = onClick)
                else this
            }
            .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_12)))
            .background(if (enabled) WableTheme.colors.purple50 else WableTheme.colors.gray200)
            .padding(top = paddingTop, bottom = paddingBottom),
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (enabled) WableTheme.colors.white else WableTheme.colors.gray600,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun WableButtonPreview() {
    WableTheme {
        Column {
            WableButton(text = "ㅎㅇㅎㅇ", onClick = {}, enabled = true)
            WableButton(text = "zzzzzzzzzzzz", onClick = {}, enabled = false)
        }
    }
}
