package com.teamwable.designsystem.type

import androidx.annotation.StringRes
import com.teamwable.designsystem.R

enum class ProfileEditType(
    @StringRes val title: Int,
    @StringRes val description: Int = R.string.empty,
    @StringRes val buttonText: Int,
) {
    ONBOARDING(
        title = R.string.onboarding_edit_title,
        description = R.string.onboarding_edit_description,
        buttonText = R.string.onboarding_edit_button,
    ),
    PROFILE(
        title = R.string.profile_edit_xml_title,
        description = R.string.empty,
        buttonText = R.string.profile_edit_button,
    ),
}
