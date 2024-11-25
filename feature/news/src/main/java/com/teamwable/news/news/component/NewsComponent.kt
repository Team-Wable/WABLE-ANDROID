package com.teamwable.news.news.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel

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

@Composable
fun WableNewsItems(
    newsItem: NewsInfoModel,
    onClick: (NewsInfoModel) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(newsItem) },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                imageModel = { newsItem.newsImage },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                ),
                modifier = Modifier
                    .widthIn(max = 100.dp)
                    .aspectRatio(1.43f)
                    .clip(RoundedCornerShape(8.dp)),
                previewPlaceholder = painterResource(id = com.teamwable.common.R.drawable.img_empty),
            )
            Column(
                modifier = Modifier.padding(
                    start = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal),
                    top = 4.dp,
                    bottom = 4.dp,
                ),
            ) {
                Text(
                    text = newsItem.newsTitle,
                    style = WableTheme.typography.body03,
                    color = WableTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = newsItem.newsText,
                    style = WableTheme.typography.body04,
                    color = WableTheme.colors.gray600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                WableNewsTimeText(newsItem.time)
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = WableTheme.colors.gray200,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val newsItem = NewsInfoModel(
        newsId = 1,
        newsTitle = "제목입니다 제목입니다 제목입니다 제..",
        newsText = "본문이 들어가는 aaaaaaaaaaaaaaaaaaaaaaa",
        newsImage = "aaaa",
        time = "2024-02-06 23:46:26",
    )
    WableTheme {
        WableNewsItems(newsItem)
    }
}
