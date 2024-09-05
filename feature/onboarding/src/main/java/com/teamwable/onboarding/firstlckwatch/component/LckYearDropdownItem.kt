package com.teamwable.onboarding.firstlckwatch.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun LckYearDropdownItem(
    year: String,
    isClicked: Boolean,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isClicked) WableTheme.colors.purple10 else WableTheme.colors.white, // 클릭 여부에 따라 색상 변경
        ),
        modifier = Modifier
            .noRippleClickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = year,
            style = if (isClicked) WableTheme.typography.body01 else WableTheme.typography.body02,
            color = if (isClicked) WableTheme.colors.purple50 else WableTheme.colors.black,
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 9.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WableExposedDropdownItemPreview() {
    WableTheme {
        LckYearDropdownItem(
            year = "2024",
            isClicked = true,
            onClick = {},
        )
    }
}
