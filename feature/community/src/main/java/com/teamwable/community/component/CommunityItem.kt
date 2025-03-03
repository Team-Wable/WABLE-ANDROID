package com.teamwable.community.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.common.type.LckTeamType
import com.teamwable.community.R
import com.teamwable.designsystem.component.button.WableSmallButton
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.miniButtonStyle

@Composable
fun CommunityItem(
    lckTeamType: LckTeamType = LckTeamType.T1,
) {
    val isEnabled = false

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Image(
            painter = painterResource(id = lckTeamType.teamProfileImage),
            modifier = Modifier
                .size(64.dp),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Text(
            text = buildString {
                append(lckTeamType.name)
                append(stringResource(R.string.str_community_lck_sub_title))
            },
            style = WableTheme.typography.head02,
            color = WableTheme.colors.black,
            modifier = Modifier
                .align(Alignment.CenterVertically),
        )
        Spacer(modifier = Modifier.weight(1f))
        WableSmallButton(
            text = stringResource(R.string.str_community_btn_text),
            buttonStyle = miniButtonStyle(),
            onClick = {},
            imageContent = {
                if (isEnabled) {
                    Image(
                        imageVector = toImageVector(id = com.teamwable.common.R.drawable.ic_community_check),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.padding(start = 2.dp))
                }
            },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CommunityItemPreview() {
    WableTheme {
        CommunityItem()
    }
}
