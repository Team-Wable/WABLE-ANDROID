package com.teamwable.onboarding.firstlckwatch.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.card.WableCustomCardWithStroke
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme
import kotlinx.collections.immutable.PersistentList

@Composable
fun WableExposedDropdownBox(
    options: PersistentList<String>,
    expanded: Boolean = false,
    selectedIndex: Int = 0,
    listState: LazyListState,
    onExpandedChange: (Boolean) -> Unit = {},
    onSelectedIndexChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        WableCustomCardWithStroke {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .noRippleClickable { onExpandedChange(!expanded) },
            ) {
                Text(
                    text = options[selectedIndex], // 선택된 연도를 표시
                    style = WableTheme.typography.body01,
                    color = WableTheme.colors.black,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterVertically),
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onExpandedChange(!expanded) },
                    modifier = Modifier.padding(end = 8.dp),
                ) {
                    Image(
                        painter = painterResource(id = com.teamwable.common.R.drawable.ic_sign_up_drop_down_btn),
                        contentDescription = "Custom Icon",
                        modifier = Modifier.rotate(if (expanded) 180f else 0f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(expanded) {
            WableCustomCardWithStroke {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    itemsIndexed(options) { index, year ->
                        LckYearDropdownItem(
                            year = year,
                            onClick = {
                                onSelectedIndexChange(index)
                                onExpandedChange(false)
                            },
                            isClicked = selectedIndex == index,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WableExposedDropdownBoxPreview() {
    WableTheme {
//        WableExposedDropdownBox(options = listOf("2024", "2025", "2026"), listState = LazyListState())
    }
}
