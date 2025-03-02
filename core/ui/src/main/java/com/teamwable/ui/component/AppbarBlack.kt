package com.teamwable.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.teamwable.ui.R
import com.teamwable.ui.databinding.AppbarBlackBinding
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.type.AppBarType

@SuppressLint("CustomViewStyleable", "Recycle")
class AppbarBlack @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: AppbarBlackBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.appbar_black, this, true)
        binding = AppbarBlackBinding.bind(view)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.AppBarBlack)
            val typeValue = typedArray.getInt(R.styleable.AppBarBlack_type, -1)
            val appBarType = AppBarType.entries.getOrNull(typeValue)
            setAppBarType(appBarType ?: return@let)
        }
    }

    private fun setAppBarType(type: AppBarType) = with(binding) {
        tvAppbarBlack.text = context.stringOf(type.title)
        ivAppbarBlack.setImageResource(type.icon)
        ivAppbarBlackBeta.visible(type.isBeta)
    }
}
