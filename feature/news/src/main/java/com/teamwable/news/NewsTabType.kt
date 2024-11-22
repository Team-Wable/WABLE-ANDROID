package com.teamwable.news

import androidx.annotation.StringRes

enum class NewsTabType(@StringRes val idx: Int) {
    MATCH(0),
    RANK(1),
    NEWS(2),
    NOTICE(3),
}
