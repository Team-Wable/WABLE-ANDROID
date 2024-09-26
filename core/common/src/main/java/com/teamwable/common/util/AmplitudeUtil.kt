package com.teamwable.common.util

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.teamwable.common.BuildConfig.AMPLITUDE_API_KEY

object AmplitudeUtil {
    private lateinit var amplitude: Amplitude

    fun initAmplitude(applicationContext: Context) {
        amplitude = Amplitude(
            Configuration(
                apiKey = AMPLITUDE_API_KEY,
                context = applicationContext,
            ),
        )
    }

    fun trackEvent(eventTag: String) {
        amplitude.track(eventTag)
    }
}
