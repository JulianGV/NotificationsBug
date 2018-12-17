package com.globant.notificarionbug.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import com.globant.notificarionbug.contact.ChatContact
import com.globant.notificarionbug.contact.ContactMessage

class NotificationItem(val threadId: Int, val text: CharSequence?, val timestamp: Long, val contact: ChatContact,
                       val contactMessage: ContactMessage) {

    fun getPendingIntent(context: Context, goToChatIntent: Intent, requestCode: Int): PendingIntent? {
        return TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(goToChatIntent)
                .getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT)
    }

}
