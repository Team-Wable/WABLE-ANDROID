package com.teamwable.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.teamwable.ui.R
import com.teamwable.ui.databinding.TextLckTeamTagBinding
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.type.TeamTag

class LckTeamTagTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: TextLckTeamTagBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = TextLckTeamTagBinding.inflate(inflater, this, true)

        if (attrs != null) getAttrs(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LckTeamTag)

        val teamName = typedArray.getString(R.styleable.LckTeamTag_team_name)

        if (teamName.isNullOrBlank()) applyTeamTagStyle(TeamTag.LCK)
        else applyTeamTagStyle(TeamTag.toTeamTag(context, teamName))

        typedArray.recycle()
    }

    private fun applyTeamTagStyle(teamTag: TeamTag) {
        binding.apply {
            tvTeamTag.backgroundTintList = ColorStateList.valueOf(context.colorOf(teamTag.backgroundColorRes))
            tvTeamTag.setTextColor(context.colorOf(teamTag.textColorRes))
            tvTeamTag.compoundDrawableTintList = ColorStateList.valueOf(context.colorOf(teamTag.textColorRes))
            tvTeamTag.text = context.stringOf(teamTag.teamName)
        }
    }
}
