package com.teamwable.viewit.benchmark

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.viewit.ViewIt
import com.teamwable.viewit.ui.ViewItActions
import com.teamwable.viewit.ui.ViewItScreen
import kotlinx.coroutines.flow.flowOf

class BenchmarkComposeEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WableTheme {
                ViewItScreen(
                    listState = rememberLazyListState(),
                    viewIts = flowOf(generateMockViewItData()).collectAsLazyPagingItems(),
                    actions = ViewItActions(),
                )
            }
        }
    }

    companion object {
        fun generateMockViewItData(count: Int = 100): PagingData<ViewIt> {
            val list = (1..count).map { index ->
                ViewIt(
                    postAuthorId = index.toLong(),
                    postAuthorProfile = "PURPLE",
                    postAuthorNickname = "nickname$index",
                    viewItId = index.toLong(),
                    linkImage = "https://mock-link-image-$index.jpg",
                    link = "https://www.naver.com/",
                    linkTitle = "link title$index",
                    linkName = "link$index",
                    viewItContent = "content",
                    isLiked = index % 3 == 0,
                    likedNumber = index.toString(),
                    isBlind = index % 20 == 0,
                )
            }

            return PagingData.from(list)
        }
    }
}
