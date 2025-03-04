package com.teamwable.news.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.button.BigButtonDefaults
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.layout.WableFloatingButtonLayout
import com.teamwable.designsystem.component.topbar.WableAppBar
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.NewsTabType
import com.teamwable.news.R
import com.teamwable.news.detail.component.NewsImage
import com.teamwable.news.news.component.WableNewsTimeText

@Composable
fun NewsDetailScreen(
    newsInfoModel: NewsInfoModel,
    type: NewsTabType,
    navigateToImageDetail: () -> Unit = {},
    navigateToBack: () -> Unit,
    navigateToGoogleForm: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            WableAppBar(
                visibility = true,
                canNavigateBack = true,
                darkTheme = true,
                canClose = false,
                title = stringResource(id = type.title),
                navigateUp = navigateToBack,
            )
        },
    ) { innerPadding ->
        WableFloatingButtonLayout(
            modifier = Modifier.padding(innerPadding),
            content = {
                NewsDetailContent(
                    newsInfoModel = newsInfoModel,
                    onClick = navigateToImageDetail,
                    type = type,
                    scrollState = scrollState,
                )
            },
            buttonContent = { modifier ->
                if (type == NewsTabType.NOTICE) {
                    WableButton(
                        text = stringResource(R.string.tv_notice_detail_button),
                        onClick = navigateToGoogleForm,
                        buttonStyle = BigButtonDefaults.blackBigButtonStyle().copy(
                            textColor = { WableTheme.colors.sky50 },
                        ),
                        modifier = modifier.padding(16.dp),
                    )
                }
            },
        )
    }
}

@Composable
fun NewsDetailContent(
    newsInfoModel: NewsInfoModel,
    type: NewsTabType,
    onClick: () -> Unit,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier.verticalScroll(scrollState),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal),
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = newsInfoModel.newsTitle,
                    style = WableTheme.typography.head01,
                    color = WableTheme.colors.black,
                    modifier = Modifier.weight(1f),
                )
                WableNewsTimeText(
                    text = newsInfoModel.time,
                    style = WableTheme.typography.caption02,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp),
                )
            }
            NewsImage(
                imageUrl = newsInfoModel.newsImage,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .noRippleClickable { onClick() },
            )

            Text(
                text = newsInfoModel.newsText,
                style = WableTheme.typography.body02,
                color = WableTheme.colors.gray800,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
        HorizontalDivider(
            color = WableTheme.colors.gray200,
            thickness = 8.dp,
            modifier = Modifier.padding(top = 18.dp),
        )
        if (type == NewsTabType.NOTICE) {
            Box(
                modifier = Modifier.aspectRatio(6.83f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        NewsDetailScreen(
            newsInfoModel = NewsInfoModel(
                newsId = 1,
                newsTitle = "LCK 2025년 변화되는 방식",
                newsImage = "adfdf",
                newsText = "어떤 순간에도 너를 찾을 수 있게 반대가 끌리는 천만번째 이유를 내일의 우리는 알지도 몰라 오늘따라 왠지 말이 꼬여 성을 빼고 부르는 건 아직 어색해 (지훈아..!) ",
                time = "2024-01-10 11:47:18",
            ),
            type = NewsTabType.NOTICE,
            navigateToBack = {},
        )
    }
}
