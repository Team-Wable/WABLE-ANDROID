package com.teamwable.viewit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun ViewitItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileImage(
                imageUrl = "GEN",
                modifier = Modifier
                    .width(28.dp)
                    .aspectRatio(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = com.teamwable.common.R.string.dummy),
                style = WableTheme.typography.body03,
                color = WableTheme.colors.black,
                modifier = Modifier.weight(1f),
            )

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = com.teamwable.common.R.drawable.ic_home_more),
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = stringResource(id = com.teamwable.common.R.string.dummy),
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

        LinkItem()
    }
}

@Composable
fun LinkItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
    ) {
        GlideImage(
            imageModel = { "" },
            previewPlaceholder = painterResource(id = com.teamwable.common.R.drawable.img_empty),
            modifier = Modifier
                .width(128.dp)
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
                text = stringResource(id = com.teamwable.common.R.string.dummy),
                style = WableTheme.typography.body03,
                color = WableTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = stringResource(id = com.teamwable.common.R.string.dummy),
                style = WableTheme.typography.caption04,
                color = WableTheme.colors.gray600,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = com.teamwable.common.R.drawable.ic_home_heart_btn_inactive),
                    contentDescription = null,
                    tint = WableTheme.colors.gray600,
                    modifier = Modifier.size(20.dp),
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "00",
                    style = WableTheme.typography.caption03,
                    color = WableTheme.colors.black,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    WableTheme {
        ViewitItem()
    }
}
