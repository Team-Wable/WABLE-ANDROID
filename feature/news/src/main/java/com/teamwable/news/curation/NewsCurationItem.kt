package com.teamwable.news.curation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.teamwable.common.R
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun NewsCurationItem() {
}

@Composable
fun CurationProfile() {
    Row {
        Image(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            imageVector = toImageVector(R.drawable.ic_share_symbol),
            contentDescription = null,
        )

        Text(
            text = "와블 큐레이터",
            style = WableTheme.typography.body03,
        )
    }
}
