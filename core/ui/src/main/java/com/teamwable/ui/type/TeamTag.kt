package com.teamwable.ui.type

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.teamwable.ui.R
import com.teamwable.ui.extensions.stringOf
import com.teamwable.common.R as r

enum class TeamTag(
    @StringRes val teamName: Int,
    @ColorRes val backgroundColorRes: Int,
    @ColorRes val textColorRes: Int,
) {
    T1(r.string.label_team_t1, R.color.t_10, R.color.t_50),
    GEN(r.string.label_team_gen, R.color.gen_10, R.color.gen_50),
    BRO(r.string.label_team_bro, R.color.bro_10, R.color.bro_50),
    DRX(r.string.label_team_drx, R.color.drx_10, R.color.drx_50),
    DK(r.string.label_team_dk, R.color.dk_10, R.color.dk_50),
    KT(r.string.label_team_kt, R.color.kt_10, R.color.kt_50),
    FOX(r.string.label_team_fox, R.color.fox_10, R.color.fox_50),
    NS(r.string.label_team_ns, R.color.ns_10, R.color.ns_50),
    KDF(r.string.label_team_kdf, R.color.kdf_10, R.color.kdf_50),
    HLE(r.string.label_team_hle, R.color.hle_10, R.color.hle_50),
    LCK(r.string.label_team_lck, R.color.white, R.color.white),
    ;

    companion object {
        fun toTeamTag(context: Context, teamName: String): TeamTag {
            return entries.find { context.stringOf(it.teamName) == teamName } ?: LCK
        }
    }
}
