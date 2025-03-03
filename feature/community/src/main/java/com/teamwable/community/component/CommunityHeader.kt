package com.teamwable.community.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.community.R
import com.teamwable.designsystem.component.card.WableShapeBox
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun CommunityHeader(modifier: Modifier = Modifier) {
    WableShapeBox(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.str_community_header),
            modifier = Modifier.padding(10.dp),
            style = WableTheme.typography.body04,
            color = WableTheme.colors.purple100,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CommunityHeaderPreview() {
    WableTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            CommunityHeader()
        }
    }
}
