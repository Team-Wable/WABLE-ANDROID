package com.teamwable.news.notice

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teamwable.designsystem.component.screen.NewsNoticeEmptyScreen
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.NewsViewModel
import com.teamwable.news.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun NewsNoticeRoute(
    viewModel: NewsViewModel = hiltViewModel(),
    navigateToDetail: (NewsInfoModel) -> Unit
) {
    val context = LocalContext.current

    viewModel.dummyNotice.apply {
        if (this.isNotEmpty()) {
            NewsNoticeScreen(context = context, notices = this, navigateToDetail = navigateToDetail)
        } else {
            NewsNoticeEmptyScreen(R.string.tv_news_notice_empty)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsNoticeScreen(
    context: Context,
    notices: List<NewsInfoModel>,
    navigateToDetail: (NewsInfoModel) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = notices, key = { item -> item.newsId }) { notice ->
            NewsNoticeItem(context, notice, navigateToDetail)
            HorizontalDivider(
                thickness = 1.dp,
                color = WableTheme.colors.gray200,
            )
        }
    }
}
