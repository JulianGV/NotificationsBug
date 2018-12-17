package com.globant.notificarionbug.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import java.util.*

class NotificationChannels {

    companion object {

        private val CATEGORY_MESSAGES = "messages"
        val MESSAGE_CHANNEL_ID = "com.excalibur.rccl.chat_notifications"
        val SUMMARY_NOTIFICATION_ID = 1338
        val NOTIFICATION_GROUP = CATEGORY_MESSAGES
        val REQUEST_CODE_PENDING_INTENT = 0


        fun create(context: Context) {
            if (!supported()) {
                return
            }

            val notificationManager = getNotificationManager(context)
            onCreate(context, notificationManager)
        }

        @TargetApi(Build.VERSION_CODES.O)
        fun getNotificationManager(context: Context): NotificationManager {
            return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        fun getMessagesChannel(): String {
            return MESSAGE_CHANNEL_ID
        }

        fun supported(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        }

        @TargetApi(Build.VERSION_CODES.O)
        private fun onCreate(context: Context, notificationManager: NotificationManager) {

            val messagesGroup = NotificationChannelGroup(CATEGORY_MESSAGES, "Messages")
            notificationManager.createNotificationChannelGroup(messagesGroup)
            val messages = NotificationChannel(getMessagesChannel(), "Default", NotificationManager.IMPORTANCE_HIGH)
            messages.group = CATEGORY_MESSAGES
            messages.enableLights(true)
            // Sets whether notification posted to this channel should vibrate.
            messages.enableVibration(true)
            // Sets the notification light color for notifications posted to this channel
            messages.lightColor = Color.BLUE
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            messages.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannels(Arrays.asList(messages))
        }
    }

}
