package com.teamwable.wable

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.teamwable.wable.BuildConfig.KAKAO_APP_KEY
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WableApp : Application() {
    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        setTimber()
        setDarkMode()
        setKaKaoSdk()
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setKaKaoSdk() {
        KakaoSdk.init(this, KAKAO_APP_KEY)
        var keyHash = Utility.getKeyHash(this)
        Timber.tag("Kakao").d("KeyHash: $keyHash")
    }
}
