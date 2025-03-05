package com.teamwable.wable.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.Constants.MessageNotificationKeys.ENABLE_NOTIFICATION
import com.google.firebase.messaging.Constants.MessageNotificationKeys.NOTIFICATION_PREFIX
import com.google.firebase.messaging.Constants.MessageNotificationKeys.NOTIFICATION_PREFIX_OLD
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teamwable.main.MainActivity
import com.teamwable.ui.extensions.colorOf
import com.teamwable.ui.util.FcmTag.CHANNEL_ID
import com.teamwable.ui.util.FcmTag.NAME
import com.teamwable.ui.util.FcmTag.NOTIFICATION_BODY
import com.teamwable.ui.util.FcmTag.NOTIFICATION_TITLE
import com.teamwable.ui.util.FcmTag.NOTIFICATION_VIEWIT_LIKE
import com.teamwable.ui.util.FcmTag.RELATED_CONTENT_ID
import timber.log.Timber

class WableFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var title: String
    private lateinit var body: String

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("fcm new token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        sendPushAlarm(
            title = if (::title.isInitialized) title else "",
            body = if (::body.isInitialized) body else "",
            contentId = message.data[RELATED_CONTENT_ID] ?: return,
            type = message.data[NAME] ?: return,
        )
    }

    private fun sendPushAlarm(title: String, body: String, contentId: String, type: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        val notificationType = when (type) {
            NOTIFICATION_VIEWIT_LIKE -> type
            else -> contentId
        }

        val notification = buildNotification(title, body, notificationType)
        notificationManager?.notify((contentId.hashCode()), notification)
    }

    override fun handleIntent(intent: Intent?) {
        val newPushAlarmIntent = intent?.apply {
            val temp = extras?.apply {
                title = getString(NOTIFICATION_TITLE).orEmpty()
                body = getString(NOTIFICATION_BODY).orEmpty()
                remove(ENABLE_NOTIFICATION)
                remove(getKeyWithOldPrefix())
            }
            replaceExtras(temp)
        }
        super.handleIntent(newPushAlarmIntent)
    }

    private fun getKeyWithOldPrefix(): String {
        val key = ENABLE_NOTIFICATION
        return if (!key.startsWith(NOTIFICATION_PREFIX)) {
            key
        } else {
            key.replace(
                NOTIFICATION_PREFIX,
                NOTIFICATION_PREFIX_OLD,
            )
        }
    }

    private fun buildNotification(
        title: String,
        body: String,
        contentId: String,
    ): Notification {
        val pendingIntent = createPendingIntent(contentId)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(com.teamwable.common.R.drawable.img_fcm_symbol)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(colorOf(com.teamwable.ui.R.color.white))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setShowWhen(true)
            .build()
    }

    private fun createPendingIntent(contentId: String): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(RELATED_CONTENT_ID, contentId)
        }
        return PendingIntent.getActivity(
            this,
            (System.currentTimeMillis()).toInt(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE,
        )
    }
}
