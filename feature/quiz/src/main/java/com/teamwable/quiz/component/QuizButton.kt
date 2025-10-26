package com.teamwable.quiz.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.component.card.WableCustomCardWithStroke
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.R

@Composable
fun QuizOXButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    type: OXType,
    onClick: () -> Unit,
) {
    WableCustomCardWithStroke(
        modifier = modifier,
        cornerRadius = 16.dp,
        strokeColor = { if (isSelected) type.strokeColor else type.backGroundColor },
        backgroundColor = type.backGroundColor,
        enabled = isSelected,
        onClick = onClick,
    ) {
        Image(
            imageVector = toImageVector(id = type.icon),
            contentDescription = null,
            modifier = Modifier
                .padding(type.imagePadding)
                .aspectRatio(1f),
        )
    }
}

enum class OXType {
    O,
    X,
    ;

    @get:DrawableRes
    val icon: Int
        get() = when (this) {
            O -> R.drawable.ic_quiz_o
            X -> R.drawable.ic_quiz_x
        }

    val backGroundColor: Color
        @Composable
        get() = when (this) {
            O -> WableTheme.colors.blue10
            X -> WableTheme.colors.red10
        }

    val strokeColor: Color
        @Composable
        get() = when (this) {
            O -> WableTheme.colors.blue50
            X -> WableTheme.colors.error
        }

    val imagePadding: Dp
        get() = when (this) {
            O -> 22.dp
            X -> 32.dp
        }
}

@Preview(showBackground = true)
@Composable
private fun QuizOXButtonPreview() {
    WableTheme {
        var type by remember { mutableStateOf<OXType?>(null) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            QuizOXButton(
                isSelected = type == OXType.O,
                type = OXType.O,
                onClick = { type = if (type == OXType.O) null else OXType.O },
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            QuizOXButton(
                isSelected = type == OXType.X,
                type = OXType.X,
                onClick = { type = if (type == OXType.X) null else OXType.X },
                modifier = Modifier.weight(1f),
            )
        }
    }
}
