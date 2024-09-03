package com.teamwable.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.TextFieldStateType

/**
 * design system small text field / Large text field 2가지 타입 구현 가능
 * */
@Composable
fun WableBasicTextField(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp),
    placeholder: String = "",
    labelText: String = "",
    value: String = "",
    onValueChange: (String) -> Unit = { _ -> },
    isCount: Boolean = false,
    state: TextFieldStateType = TextFieldStateType.DEFAULT,
    maxLines: Int = 1,
    minLines: Int = 1,
    maxLength: Int = 10,
    minHeight: Dp = 0.dp,
    textStyle: TextStyle = WableTheme.typography.body02,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            if (newValue.replace(" ", "").length <= maxLength) onValueChange(newValue)
        },
        singleLine = maxLines == 1,
        textStyle = textStyle.copy(WableTheme.colors.black),
        maxLines = if (minLines > maxLines) minLines else maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(WableTheme.colors.purple50),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        decorationBox = { innerText ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .heightIn(minHeight)
                        .fillMaxWidth()
                        .clip(shape = shape)
                        .background(color = WableTheme.colors.gray200)
                        .padding(vertical = 11.dp, horizontal = 16.dp),
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = WableTheme.colors.gray500,
                            style = WableTheme.typography.body02,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                        )
                    }
                    innerText()
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    Text(
                        text = labelText,
                        modifier = Modifier.align(Alignment.CenterStart),
                        color = state.labelTextColor(),
                        style = WableTheme.typography.caption02,
                        maxLines = 1,
                    )
                    Text(
                        text = if (isCount) "${value.length} / $maxLength" else "",
                        modifier = Modifier.align(Alignment.CenterEnd),
                        color = WableTheme.colors.gray400,
                        style = WableTheme.typography.caption02,
                        maxLines = 1,
                    )
                }
            }
        },
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun TextFieldPreview() {
    WableTheme {
        var normalValue by remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .background(Color.Black)
                .padding(vertical = 10.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            WableBasicTextField(
                placeholder = "예) 중꺾마",
                labelText = "10자리 이내, 문자/숫자로 입력 가능해요",
                value = normalValue,
                onValueChange = { normalValue = it },
            )
            WableBasicTextField(
                placeholder = "예) 중꺾마",
                state = TextFieldStateType.ERROR,
                value = normalValue,
                labelText = "닉네임에 사용할 수 없는 문자가 포함되어 있어요.",
                onValueChange = { normalValue = it },
            )
            WableBasicTextField(
                placeholder = "예) 중꺾마",
                state = TextFieldStateType.SUCCESS,
                value = normalValue,
                labelText = "사용 가능한 닉네임입니다.",
                onValueChange = { normalValue = it },
            )

            WableBasicTextField(
                placeholder = "예) 중꺾마",
                maxLines = 20,
                maxLength = 300,
                minHeight = 148.dp,
                value = normalValue,
                onValueChange = { normalValue = it },
            )
        }
    }
}
