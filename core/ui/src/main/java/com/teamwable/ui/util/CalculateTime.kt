package com.teamwable.ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class CalculateTime {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTime(inputDateTime: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")

        return LocalDateTime.parse(inputDateTime, inputFormatter).format(outputFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isToday(inputDate: String): Boolean {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(inputDate, dateFormatter).isEqual(LocalDate.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("MM. dd (E)")

        val date = LocalDate.parse(inputDate, inputFormatter)
        val koreanDayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ko", "KR"))

        return date.format(outputFormatter).replace(date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), koreanDayOfWeek)
    }
}
