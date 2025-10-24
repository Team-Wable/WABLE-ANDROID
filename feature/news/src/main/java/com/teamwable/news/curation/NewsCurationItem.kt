package com.teamwable.news.curation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.common.R
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.CurationModel
import com.teamwable.news.news.component.WableNewsTimeText

@Composable
fun NewsCurationItem(
    data: CurationModel,
    onItemClick: (String) -> Unit,
) {
    Column {
        CurationProfile(data)
        Spacer(modifier = Modifier.height(8.dp))
        CurationContent(data, onItemClick)
    }
}

@Composable
fun CurationProfile(
    data: CurationModel,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            imageVector = toImageVector(R.drawable.ic_share_symbol),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "와블 큐레이터",
            style = WableTheme.typography.body03,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "· ",
            style = WableTheme.typography.caption04,
            color = WableTheme.colors.gray500,
        )
        WableNewsTimeText(data.time)
    }
}

@Composable
fun CurationContent(
    data: CurationModel,
    onItemClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onItemClick(data.curationLink) },
    ) {
        GlideImage(
            imageModel = { data.curationThumbnail },
            previewPlaceholder = painterResource(id = R.drawable.img_empty),
            loading = {
                Box(
                    modifier = Modifier
                        .background(WableTheme.colors.gray200),
                )
            },
            failure = {
                Image(
                    painter = painterResource(id = R.drawable.img_view_it_empty),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                    ),
                )
                .aspectRatio(2.1f),
        )

        Row(
            modifier = Modifier
                .background(
                    color = WableTheme.colors.gray100,
                    shape = RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                    ),
                )
                .border(
                    width = 1.dp,
                    color = WableTheme.colors.gray200,
                    shape = RoundedCornerShape(
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp,
                    ),
                )
                .padding(vertical = 10.dp, horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1.1f),
            ) {
                Text(
                    text = data.curationTitle,
                    style = WableTheme.typography.body03,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = data.curationLink,
                    style = WableTheme.typography.body04,
                    color = WableTheme.colors.gray600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Icon(
                painter = painterResource(id = com.teamwable.news.R.drawable.ic_curation_link),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(32.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CurationItemPreview() {
    WableTheme {
        Column {
            NewsCurationItem(
                CurationModel(
                    5,
                    "https://game.naver.com/esports/League_of_Legends/videos/1047418",
                    "네이버 e스포츠",
                    "https://nng-phinf.pstatic.net/MjAyNTEwMTlfMTgw/MDAxNzYwODc2NDg1NTQ5.ahwRkEYatqCqDV4zQaljNX-g3DcL-KuEIa8AChLT600g.GYnPg-OW1ySjWwwK3fvMGMxtX1LosyyaKBzTkViHY4Ag.JPEG/2_VKS-PSG_2%EC%84%B8%ED%8A%B8%E3%85%A310.19%E3%85%A32025_LoL_%EC%9B%94%EB%93%9C_%EC%B1%94%ED%94%BC%EC%96%B8%EC%8B%AD_%EC%8A%A4%EC%9C%84%EC%8A%A4_%EC%8A%A4%ED%85%8C%EC%9D%B4%EC%A7%80.jpg",
                    "2025-10-20 21:20:40",
                ),
                {},
            )
        }
    }
}
