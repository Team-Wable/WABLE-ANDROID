package com.teamwable.viewit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.ui.type.BasicProfileType

@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    if (imageUrl.isNullOrBlank()) return

    val profileType = BasicProfileType.entries.find { it.name == imageUrl }

    if (profileType != null) {
        Image(
            painter = painterResource(id = profileType.profileDrawableRes),
            modifier = Modifier.clip(CircleShape),
            contentDescription = null,
        )
    } else {
        GlideImage(
            imageModel = { imageUrl },
            modifier = modifier.clip(CircleShape),
            previewPlaceholder = painterResource(id = com.teamwable.common.R.drawable.img_empty),
        )
    }
}
