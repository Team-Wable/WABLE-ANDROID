package com.teamwable.common.type

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teamwable.common.R

enum class LckTeamType(
    @StringRes val teamName: Int,
    @DrawableRes val teamProfileImage: Int,
    @ColorRes val backgroundColorRes: Int,
    @ColorRes val textColorRes: Int,
) {
    T1(
        R.string.label_team_t1,
        R.drawable.ic_news_team_profile_t1,
        R.color.t_10,
        R.color.t_50,
    ),
    GEN(
        R.string.label_team_gen,
        R.drawable.ic_news_team_profile_gen,
        R.color.gen_10,
        R.color.gen_50,
    ),
    BRO(
        R.string.label_team_bro,
        R.drawable.ic_news_team_profile_bro,
        R.color.bro_10,
        R.color.bro_50,
    ),
    DRX(
        R.string.label_team_drx,
        R.drawable.ic_news_team_profile_drx,
        R.color.drx_10,
        R.color.drx_50,
    ),
    DK(
        R.string.label_team_dk,
        R.drawable.ic_news_team_profile_dk,
        R.color.dk_10,
        R.color.dk_50,
    ),
    KT(
        R.string.label_team_kt,
        R.drawable.ic_news_team_profile_kt,
        R.color.kt_10,
        R.color.kt_50,
    ),
    BFX(
        R.string.label_team_bfx,
        R.drawable.ic_news_team_profile_bfx,
        R.color.bfx_10,
        R.color.bfx_50,
    ),
    NS(
        R.string.label_team_ns,
        R.drawable.ic_news_team_profile_ns,
        R.color.ns_10,
        R.color.ns_50,
    ),
    DNF(
        R.string.label_team_dnf,
        R.drawable.ic_news_team_profile_dnf,
        R.color.dnf_10,
        R.color.dnf_50,
    ),
    HLE(
        R.string.label_team_hle,
        R.drawable.ic_news_team_profile_hle,
        R.color.hle_10,
        R.color.hle_50,
    ),
    ;

    companion object {
        fun toTeamTag(context: Context, teamName: String): LckTeamType? {
            return entries.find { context.getString(it.teamName) == teamName }
        }
    }
}
