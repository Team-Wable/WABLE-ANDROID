package com.teamwable.news.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import com.teamwable.designsystem.extension.modifier.noRippleDebounceClickable
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.news.NewsTabType

@Composable
fun NoticeDetailButton(
    text: String,
    textColor: Color = WableTheme.colors.sky50,
    backgroundColor: Color = WableTheme.colors.black,
    textStyle: TextStyle = WableTheme.typography.body03,
    aspectRatio: Float = 6.83f,
    type: NewsTabType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (type == NewsTabType.NOTICE) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .run {
                    noRippleDebounceClickable(onClick = onClick)
                }
                .clip(RoundedCornerShape(dimensionResource(com.teamwable.designsystem.R.dimen.radius_12)))
                .background(backgroundColor),
        ) {
            Text(
                text =  text,
                style = textStyle,
                color = textColor,
            )
        }
    }
}
