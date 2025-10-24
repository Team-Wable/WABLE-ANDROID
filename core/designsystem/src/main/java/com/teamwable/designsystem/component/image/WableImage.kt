package com.teamwable.designsystem.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.designsystem.extension.modifier.noRippleThrottleClickable
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableGlideImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    onClick: () -> Unit = {},
) {
    GlideImage(
        imageModel = { imageUrl },
        imageOptions = ImageOptions(
            contentScale = contentScale,
            alignment = alignment,
        ),
        previewPlaceholder = painterResource(id = com.teamwable.common.R.drawable.img_empty),
        modifier = modifier.noRippleThrottleClickable(onClick = onClick),
        failure = {
            Box(
                modifier = Modifier.background(WableTheme.colors.gray200),
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun WableGlideImagePreview() {
    WableGlideImage(
        modifier = Modifier.size(100.dp),
        imageUrl = "",
        contentScale = ContentScale.Crop,
        onClick = {},
    )
}
