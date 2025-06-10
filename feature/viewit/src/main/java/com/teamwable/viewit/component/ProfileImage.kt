package com.teamwable.viewit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.ui.type.BasicProfileType

@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    val profileType = BasicProfileType.entries.find { it.name == imageUrl }

    if (profileType != null) {
        Image(
            painter = painterResource(id = profileType.profileDrawableRes),
            modifier = modifier.clip(CircleShape),
            contentDescription = null,
        )
    } else {
        GlideImage(
            imageModel = { imageUrl },
            modifier = modifier.clip(CircleShape),
            loading = {
                Box(
                    modifier = modifier
                        .background(WableTheme.colors.gray200)
                        .clip(CircleShape),
                )
            },
            failure = {
                Image(
                    painter = painterResource(id = com.teamwable.common.R.drawable.img_empty),
                    modifier = modifier.clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            },
        )
    }
}
