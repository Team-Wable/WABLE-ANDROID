package com.teamwable.ui.component

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.teamwable.common.type.LckTeamType
import com.teamwable.ui.databinding.TextLckTeamTagBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf

class LckTeamTagTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: TextLckTeamTagBinding

    var teamName: String? = null
        set(value) {
            field = value
            updateTeamTagStyle()
        }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = TextLckTeamTagBinding.inflate(inflater, this, true)
    }

    private fun updateTeamTagStyle() {
        if (teamName.isNullOrBlank()) return
        val tag = LckTeamType.toTeamTag(context, teamName!!) ?: return
        applyTeamTagStyle(tag)
    }

    private fun applyTeamTagStyle(teamTag: LckTeamType) {
        binding.apply {
            tvTeamTag.backgroundTintList = ColorStateList.valueOf(context.colorOf(teamTag.backgroundColorRes))
            tvTeamTag.setTextColor(context.colorOf(teamTag.textColorRes))
            tvTeamTag.compoundDrawableTintList = ColorStateList.valueOf(context.colorOf(teamTag.textColorRes))
            tvTeamTag.text = context.stringOf(teamTag.teamName)
        }
    }
}
