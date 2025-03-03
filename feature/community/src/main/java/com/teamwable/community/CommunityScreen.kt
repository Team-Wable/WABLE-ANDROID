package com.teamwable.community

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.common.type.LckTeamType
import com.teamwable.community.component.CommunityHeader
import com.teamwable.community.component.CommunityItem
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.ContentType

@Composable
fun CommunityRoute() {
    CommunityScreen()
}

@Composable
private fun CommunityScreen() {
    var selectedTeam by rememberSaveable { mutableStateOf<LckTeamType?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal)),
    ) {
        item(
            contentType = ContentType.Banner.name,
        ) {
            CommunityHeader(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 8.dp,
                ),
            )
        }
        items(
            items = LckTeamType.entries,
            key = { items -> items.name },
            contentType = { ContentType.Item.name },
        ) { item ->
            val isSelected = item == selectedTeam
            CommunityItem(
                lckTeamType = item,
                isCommunity = item == selectedTeam,
                onClick = {
                    selectedTeam = if (isSelected) null else item
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityPreview() {
    WableTheme {
        CommunityScreen()
    }
}
