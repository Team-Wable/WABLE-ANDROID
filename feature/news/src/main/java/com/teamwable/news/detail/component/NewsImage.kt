package com.teamwable.news.detail.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NewsImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    if (!imageUrl.isNullOrBlank()) {
        GlideImage(
            imageModel = { imageUrl },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            ),
            modifier = modifier.clip(RoundedCornerShape(8.dp)),
            previewPlaceholder = painterResource(id = com.teamwable.common.R.drawable.img_empty),
        )
    }
}
