package com.teamwable.quiz.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamwable.quiz.R

enum class QuizResultType {
    SUCCESS,
    FAIL,
    ;

    @get:DrawableRes
    val image: Int
        get() = when (this) {
            SUCCESS -> R.drawable.img_quiz_success
            FAIL -> R.drawable.img_quiz_fail
        }

    @get:StringRes
    val title: Int
        get() = when (this) {
            SUCCESS -> R.string.str_quiz_result_success_title
            FAIL -> R.string.str_quiz_result_fail_title
        }

    @get:StringRes
    val description: Int
        get() = when (this) {
            SUCCESS -> R.string.str_quiz_result_success_description
            FAIL -> R.string.str_quiz_result_fail_description
        }
}
