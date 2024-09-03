package com.teamwable.onboarding.agreeterms.model

import androidx.annotation.StringRes
import com.teamwable.onboarding.R

enum class AgreeTerm(@StringRes val label: Int) {
    TERMS_OF_SERVICE(R.string.agree_term_terms_of_service),
    PRIVACY_POLICY(R.string.agree_term_privacy_policy),
    AGE_CONFIRMATION(R.string.agree_term_age_confirmation),
    MARKETING_CONSENT(R.string.agree_term_marketing_consent),
}
