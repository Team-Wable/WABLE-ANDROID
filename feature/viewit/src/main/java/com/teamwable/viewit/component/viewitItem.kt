package com.teamwable.viewit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.viewit.ViewIt

@Composable
fun ViewitItem(
    viewIt: ViewIt,
    onClickProfile: (ViewIt) -> Unit,
    onClickKebab: (ViewIt) -> Unit,
    onClickLink: (ViewIt) -> Unit,
    onClickLike: (ViewIt) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileImage(
                imageUrl = viewIt.postAuthorProfile,
                modifier = Modifier
                    .width(28.dp)
                    .aspectRatio(1f)
                    .clickable { onClickProfile(viewIt) },
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = viewIt.postAuthorNickname,
                    style = WableTheme.typography.body03,
                    color = WableTheme.colors.black,
                    modifier = Modifier
                        .wrapContentWidth()
                        .clickable { onClickProfile(viewIt) },
                )
            }

            IconButton(
                onClick = { onClickKebab(viewIt) },
                modifier = Modifier.noRippleClickable(),
            ) {
                Icon(
                    painter = painterResource(id = com.teamwable.common.R.drawable.ic_home_more),
                    contentDescription = null,
                    tint = WableTheme.colors.gray500,
                )
            }
        }

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = viewIt.viewItContent,
            style = WableTheme.typography.body04,
            color = WableTheme.colors.dk50,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = WableTheme.colors.purple10,
                    shape = RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp,
                    ),
                )
                .padding(
                    horizontal = 7.dp,
                    vertical = 10.dp,
                ),
        )

        Spacer(modifier = Modifier.height(9.dp))

        LinkItem(viewIt, onClickLink, onClickLike)
    }
}

@Composable
fun LinkItem(
    viewIt: ViewIt,
    onClickLink: (ViewIt) -> Unit,
    onClickLike: (ViewIt) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clickable { onClickLink(viewIt) },
    ) {
        GlideImage(
            imageModel = { viewIt.linkImage },
            previewPlaceholder = painterResource(id = com.teamwable.common.R.drawable.img_empty),
            modifier = Modifier
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                    ),
                )
                .aspectRatio(1.6f),
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
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
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                    ),
                )
                .padding(8.dp),
        ) {
            Text(
                text = viewIt.linkTitle,
                style = WableTheme.typography.body03,
                color = WableTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = viewIt.linkName,
                style = WableTheme.typography.caption04,
                color = WableTheme.colors.gray600,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = com.teamwable.common.R.drawable.ic_home_heart_btn_inactive),
                    contentDescription = null,
                    tint = WableTheme.colors.gray600,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onClickLike(viewIt) },
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = viewIt.likedNumber,
                    style = WableTheme.typography.caption03,
                    color = WableTheme.colors.black,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewItItemPreview() {
    WableTheme {
        ViewitItem(
            ViewIt(
                postAuthorId = 1,
                postAuthorProfile = "PURPLE",
                postAuthorNickname = "프리뷰유저",
                viewItId = 102,
                linkImage = "",
                link = "https://example.com",
                linkTitle = "프리뷰 링크 제목",
                linkName = "프리뷰 링크",
                viewItContent = "프리뷰용 테스트 콘텐츠입니다.",
                isLiked = false,
                likedNumber = "45",
                isBlind = false,
            ),
            onClickProfile = {},
            onClickKebab = {},
            onClickLike = {},
            onClickLink = {},
        )
    }
}
