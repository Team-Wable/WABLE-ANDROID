package com.teamwable.designsystem.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamwable.common.R as r

enum class LckTeamType(
    @StringRes val teamName: Int,
    @DrawableRes val teamProfileImage: Int,
) {
    T1(r.string.label_team_t1, com.teamwable.common.R.drawable.ic_news_team_profile_t1),
    GEN(r.string.label_team_gen, com.teamwable.common.R.drawable.ic_news_team_profile_gen),
    BRO(r.string.label_team_bro, com.teamwable.common.R.drawable.ic_news_team_profile_bro),
    DRX(r.string.label_team_drx, com.teamwable.common.R.drawable.ic_news_team_profile_drx),
    DK(r.string.label_team_dk, com.teamwable.common.R.drawable.ic_news_team_profile_dk),
    KT(r.string.label_team_kt, com.teamwable.common.R.drawable.ic_news_team_profile_kt),
    FOX(r.string.label_team_fox, com.teamwable.common.R.drawable.ic_news_team_profile_fox),
    NS(r.string.label_team_ns, com.teamwable.common.R.drawable.ic_news_team_profile_ns),
    KDF(r.string.label_team_kdf, com.teamwable.common.R.drawable.ic_news_team_profile_kdf),
    HLE(r.string.label_team_hle, com.teamwable.common.R.drawable.ic_news_team_profile_hle),
}
