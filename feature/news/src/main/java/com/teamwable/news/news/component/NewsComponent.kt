package com.teamwable.news.news.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.teamwable.designsystem.extension.composable.toImageVector

@Composable
fun WableNewsTopBanner(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Image(
            imageVector = toImageVector(id = com.teamwable.common.R.drawable.ic_news_top_banner),
            contentDescription = null,
            modifier = Modifier.aspectRatio(5.05f),
        )
        Box(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal))
                .align(Alignment.CenterStart),
        ) {
            content()
        }
    }
}
