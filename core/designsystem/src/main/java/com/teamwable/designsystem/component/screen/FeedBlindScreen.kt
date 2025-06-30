package com.teamwable.designsystem.component.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.R
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun FeedBlindScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3.4f)
            .background(
                color = WableTheme.colors.gray200,
                shape = RoundedCornerShape(8.dp),
            ),
    ) {
        Image(
            painter = painterResource(id = com.teamwable.common.R.drawable.ic_share_blind),
            contentDescription = null,
        )
        Text(
            style = WableTheme.typography.body01,
            color = WableTheme.colors.gray600,
            text = stringResource(id = R.string.feed_blind_description),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedBlindScreenPreview() {
    WableTheme {
        FeedBlindScreen()
    }
}
