package com.globant.notificarionbug.notification

import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.globant.notificarionbug.MainActivity
import com.globant.notificarionbug.R
import com.globant.notificarionbug.capitalizeAllWords
import com.globant.notificarionbug.contact.ChatContact
import com.globant.notificarionbug.contact.ContactMessage
import com.globant.notificarionbug.notification.NotificationChannels.Companion.MESSAGE_CHANNEL_ID
import com.globant.notificarionbug.notification.NotificationChannels.Companion.NOTIFICATION_GROUP
import com.globant.notificarionbug.notification.NotificationChannels.Companion.SUMMARY_NOTIFICATION_ID

class ChatMessageNotification {

    companion object {

        private var notificationState = NotificationState()
        private val MIN_NOTIFICATIONS_SIZE = 1
        private val TO_INDEX_MESSAGE = 9
        private val FROM_INDEX_MESSAGE = 0

        fun updateNotification(context: Context, contact: ChatContact,
                               contactMessage: ContactMessage) {
            val notificationState = constructNotificationState(contact, contactMessage)
            if(notificationState.threads.size >= MIN_NOTIFICATIONS_SIZE) {
                if (notificationState.hasMultipleThreads()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        for (threadId in notificationState.threads) {
                            val notificationStateSave = NotificationState(notificationState.getNotificationsForThread(threadId))
                            if (notificationStateSave.getNotifications().size >= MIN_NOTIFICATIONS_SIZE) {
                                val contactSave = notificationStateSave.getNotifications()[0].contact
                                val contactMessageSave = notificationStateSave.getNotifications()[0].contactMessage
                                sendSingleThreadNotification(context,
                                        notificationStateSave,
                                        true,
                                        contactSave,
                                        contactMessageSave)
                            }
                        }
                    }

                    sendMultipleThreadNotification(context, notificationState)
                } else {
                    sendSingleThreadNotification(context, notificationState, false, contact, contactMessage)
                }
            }
        }

        private fun sendSingleThreadNotification(context: Context,
                                                 notificationState: NotificationState,
                                                 bundled: Boolean,
                                                 contact: ChatContact,
                                                 contactMessage: ContactMessage) {

            if (notificationState.getNotifications().isEmpty()) {
                if (!bundled) cancelActiveNotifications(context)
                return
            }

            val res = context.resources
            val messageString = res.getQuantityString(R.plurals.notification_message_word,
                    contactMessage.messages.size, contactMessage.messages.size)

            val builder = SingleRecipientNotificationBuilder(context, MESSAGE_CHANNEL_ID)
            val notifications = notificationState.getNotifications()
            val notificationId = SUMMARY_NOTIFICATION_ID + if (bundled) notifications[0].threadId else 0

            builder.setThread("${contactMessage.name.capitalizeAllWords()} (${contactMessage.messages.size} $messageString)")
            builder.setMessageCount(notificationState.messageCount)
            builder.setPrimaryMessageBody(notifications[0].text!!)

            builder.setContentIntent(notifications[0].getPendingIntent(context,
                    MainActivity.getIntentContactListNotificationFlow(context, contact, true),
                    contactMessage.uniqueId))
            builder.setGroup(NOTIFICATION_GROUP)
            builder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)

            val timestamp = notifications[0].timestamp
            if (timestamp != 0L) builder.setWhen(timestamp)

            val lastMessages = if(notifications.size > TO_INDEX_MESSAGE)
                notifications.subList(FROM_INDEX_MESSAGE, TO_INDEX_MESSAGE).reversed()
            else notifications.reversed()

            val iterator = lastMessages.
                    listIterator(lastMessages.size)

            while (iterator.hasPrevious()) {
                val item = iterator.previous()
                builder.addMessageBody(item.text)
            }

            if (!bundled) {
                builder.setGroupSummary(true)
            }

            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        }

        private fun sendMultipleThreadNotification(context: Context,
                                                   notificationState: NotificationState) {
            val builder = MultipleRecipientNotificationBuilder(context, MESSAGE_CHANNEL_ID)
            val notifications = notificationState.getNotifications()

            builder.setMessageCount(notificationState.messageCount, notificationState.threadCount)
            builder.setGroup(NOTIFICATION_GROUP)
            builder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)

            val timestamp = notifications[0].timestamp
            if (timestamp != 0L) builder.setWhen(timestamp)

            val iterator = notifications.listIterator(notifications.size)

            while (iterator.hasPrevious()) {
                val item = iterator.previous()
                builder.addMessageBody(item.text)
            }

            NotificationManagerCompat.from(context).notify(SUMMARY_NOTIFICATION_ID, builder.build())
        }

        private fun constructNotificationState(contact: ChatContact,
                                               contactMessage: ContactMessage): NotificationState {
            val threadId = contactMessage.uniqueId
            val body = contact.lastMessage
            val timestamp = contact.lastMessageTimestamp

            notificationState.addNotification(NotificationItem(threadId, body, timestamp, contact, contactMessage))

            return notificationState
        }

        fun cancelActiveNotifications(context: Context) {
            val notifications = NotificationChannels.getNotificationManager(context)
            notifications.cancel(SUMMARY_NOTIFICATION_ID)
            notificationState = NotificationState()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    val activeNotifications = notifications.activeNotifications

                    for (activeNotification in activeNotifications) {
                        notifications.cancel(activeNotification.id)
                    }
                } catch (e: Throwable) {
                    notifications.cancelAll()
                }

            }
        }
    }

}
