package com.teamwable.ui.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.teamwable.ui.R
import com.teamwable.ui.type.BasicProfileType

fun ImageView.load(url: String?) {
    if (!url.isNullOrEmpty()) {
        val profileType = BasicProfileType.entries.find { it.name == url }

        if (profileType != null) {
            Glide.with(this)
                .load(profileType.profileDrawableRes)
                .into(this)
        } else {
            Glide.with(this)
                .load(url)
                .placeholder(R.color.gray_400)
                .error(com.teamwable.common.R.drawable.ic_home_toast_error)
                .into(this)
        }
    } else {
        setImageResource(com.teamwable.common.R.drawable.img_empty)
    }
}
