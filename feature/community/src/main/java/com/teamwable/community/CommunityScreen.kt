package com.teamwable.community

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.teamwable.community.component.CommunityButtonType
import com.teamwable.community.component.CommunityHeader
import com.teamwable.community.component.CommunityItem
import com.teamwable.designsystem.component.button.BigButtonDefaults
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.layout.WableFloatingButtonLayout
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.ContentType

@Composable
fun CommunityRoute() {
    CommunityScreen()
}

@Composable
private fun CommunityScreen() {
    var selectedTeam by rememberSaveable { mutableStateOf<LckTeamType?>(null) }
    var selectedState by rememberSaveable { mutableStateOf(CommunityButtonType.DEFAULT) }

    WableFloatingButtonLayout(
        buttonContent = { modifier ->
            WableButton(
                text = "dsfdsfsdf",
                onClick = {},
                modifier = modifier.padding(
                    horizontal = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal),
                    vertical = 14.dp,
                ),
                buttonStyle = BigButtonDefaults.blackBigButtonStyle(),
            )
        },
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal),
                end = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal),
                top = 10.dp,
                bottom = 64.dp,
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            item(
                contentType = ContentType.Banner.name,
            ) {
                CommunityHeader()
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(
                items = LckTeamType.entries,
                key = { items -> items.name },
                contentType = { ContentType.Item.name },
            ) { item ->
                val isSelected = item == selectedTeam
                CommunityItem(
                    lckTeamType = item,
                    type = if (isSelected) selectedState else CommunityButtonType.DEFAULT,
                    enabled = selectedTeam == null || isSelected,
                    onClick = {
                        when {
                            selectedTeam == null -> {
                                selectedTeam = item
                                selectedState = CommunityButtonType.FAN
                            }

                            isSelected -> {
                                selectedState = when (selectedState) {
                                    CommunityButtonType.FAN -> CommunityButtonType.COMPLETED
                                    else -> selectedState
                                }
                            }
                        }
                    },
                )
            }
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
