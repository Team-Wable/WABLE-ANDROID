package com.teamwable.model.quiz

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizResultModel(
    val time: Int = 0,
    val quizResult: Boolean = false,
    val userPercent: Int = 0,
    val continueNumber: Int = 0,
) : Parcelable
