package com.teamwable.quiz.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.card.WableShapeBox
import com.teamwable.designsystem.component.card.radius16Style
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun RowScope.QuizStatBox(
    title: String,
    titleColor: Color,
    value: String,
) {
    WableShapeBox(
        shapeStyle = radius16Style().copy(backgroundColor = WableTheme.colors.black),
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = title,
                style = WableTheme.typography.body03,
                color = titleColor,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = WableTheme.typography.head00,
                color = WableTheme.colors.white,
                textAlign = TextAlign.Center,
            )
        }
    }
}
