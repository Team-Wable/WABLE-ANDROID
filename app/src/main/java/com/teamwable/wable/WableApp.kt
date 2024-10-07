package com.teamwable.wable

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import com.teamwable.common.util.AmplitudeUtil.initAmplitude
import com.teamwable.ui.util.FcmTag.CHANNEL_ID
import com.teamwable.ui.util.FcmTag.CHANNEL_NAME
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
        createNotificationChannel()
        initAmplitude(context)
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
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                IMPORTANCE_HIGH,
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
