package com.teamwable.news.notice

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.ui.util.CalculateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsNoticeItem(
    context: Context,
    data: NewsInfoModel,
    navigateToDetail: (NewsInfoModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WableTheme.colors.white)
            .clickable { navigateToDetail(data) }
            .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        Row {
            Text(text = data.newsTitle, style = WableTheme.typography.body01)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = CalculateTime().getCalculateTime(context, data.time), color = WableTheme.colors.gray500, style = WableTheme.typography.caption04)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = data.newsText, color = WableTheme.colors.gray600, maxLines = 2, style = WableTheme.typography.body04)
    }
}
