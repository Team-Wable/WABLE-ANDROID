package com.teamwable.quiz.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.quiz.R

enum class QuizStatType {
    XP,
    RANK,
    SPEED,
    ;

    @get:StringRes
    val title: Int
        get() = when (this) {
            XP -> R.string.str_quiz_stat_xp
            RANK -> R.string.str_quiz_stat_rank
            SPEED -> R.string.str_quiz_stat_speed
        }

    val titleColor: Color
        @Composable
        get() = when (this) {
            XP -> WableTheme.colors.sky50
            RANK -> WableTheme.colors.blue50
            SPEED -> WableTheme.colors.purple50
        }
}
