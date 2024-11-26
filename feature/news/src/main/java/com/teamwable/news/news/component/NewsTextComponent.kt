package com.teamwable.news.news.component

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.ui.util.FeedTransformer

@SuppressLint("NewApi")
@Composable
fun WableNewsTimeText(
    text: String,
    style: TextStyle = WableTheme.typography.caption04,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Text(
        text = FeedTransformer.time.getCalculateTime(context, text),
        style = style,
        color = WableTheme.colors.gray500,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}
