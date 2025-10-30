package com.teamwable.onboarding.selectlckteam.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.common.type.LckTeamType
import com.teamwable.designsystem.component.card.WableCustomCardWithStroke
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun LckTeamItem(
    lckTeamType: LckTeamType = LckTeamType.T1,
    enabled: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    WableCustomCardWithStroke(
        cornerRadius = 32.dp,
        enabled = enabled,
        onClick = onClick,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp,
                ),
            verticalAlignment = Alignment.CenterVertically, // 수직 중앙 정렬 설정
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Image(
                painter = painterResource(id = lckTeamType.teamProfileImage),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = lckTeamType.teamName),
                style = WableTheme.typography.body01,
                color = if (enabled) WableTheme.colors.black else WableTheme.colors.gray700,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WableCustomCardWithStrokePreview() {
    WableTheme {
        LckTeamItem()
    }
}
