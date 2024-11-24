package com.teamwable.news.notice

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.news.NewsViewModel
import com.teamwable.news.model.NewsInfoModel

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
            NewsNoticeEmptyScreen()
        }
    }
}

@Composable
fun NewsNoticeScreen(
    context: Context,
    notices: List<NewsInfoModel>,
    navigateToDetail: (NewsInfoModel) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notices) { notice ->
                NewsNoticeItem(context, notice, navigateToDetail)
                HorizontalDivider(
                    thickness = 1.dp,
                    color = WableTheme.colors.gray200,
                )
            }
        }
    }
}

@Composable
fun NewsNoticeEmptyScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "아직 작성된 공지사항이 없어요.", color = WableTheme.colors.gray500, style = WableTheme.typography.body02)
    }
}
