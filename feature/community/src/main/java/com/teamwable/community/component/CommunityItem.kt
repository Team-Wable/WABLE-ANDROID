package com.teamwable.community.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.common.type.LckTeamType
import com.teamwable.community.R
import com.teamwable.designsystem.component.button.WableSmallButton
import com.teamwable.designsystem.component.indicator.WableLinearProgressBar
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.SmallButtonDefaults

@Composable
fun CommunityItem(
    lckTeamType: LckTeamType = LckTeamType.T1,
    type: CommunityButtonType = CommunityButtonType.DEFAULT,
    enabled: Boolean = true,
    progress: Float = 0.0f,
    onClick: () -> Unit = {},
) {
    val backgroundColor = if (type == CommunityButtonType.DEFAULT) {
        WableTheme.colors.white
    } else {
        WableTheme.colors.gray100
    }
    Column(
        modifier = Modifier
            .background(backgroundColor)
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
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
                text = stringResource(type.message),
                enabled = enabled,
                buttonStyle = SmallButtonDefaults.miniButtonStyle(),
                onClick = onClick,
                imageContent = {
                    if (type == CommunityButtonType.COMPLETED) {
                        Image(
                            imageVector = toImageVector(id = com.teamwable.common.R.drawable.ic_community_check),
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.padding(start = 2.dp))
                    }
                },
            )
        }
        if (type != CommunityButtonType.DEFAULT) {
            Spacer(Modifier.padding(top = 10.dp))
            WableLinearProgressBar(
                progress = { progress / 100 },
                content = {
                    CommunityProgressTitle()
                    Spacer(Modifier.padding(bottom = 4.dp))
                },
            )
        }
    }
}

@Composable
fun CommunityProgressTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.str_community_progress_title),
            style = WableTheme.typography.caption01,
            color = WableTheme.colors.black,
        )
        Image(
            imageVector = toImageVector(id = com.teamwable.common.R.drawable.ic_home_tag_team_fire),
            contentDescription = null,
            colorFilter = ColorFilter.tint(WableTheme.colors.t50),
            modifier = Modifier.size(16.dp),
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
