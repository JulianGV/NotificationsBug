package com.globant.notificarionbug.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.globant.notificarionbug.MainActivity
import com.globant.notificarionbug.R
import com.globant.notificarionbug.notification.NotificationChannels.Companion.REQUEST_CODE_PENDING_INTENT
import java.util.*

class MultipleRecipientNotificationBuilder(context: Context, channelId: String) : AbstractNotificationBuilder(context, channelId) {

    private val messageBodies = LinkedList<CharSequence>()

    init {

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        setSound(defaultSoundUri)
        setSmallIcon(R.mipmap.ic_launcher)
        setContentTitle(context.getString(R.string.app_name))
        setContentIntent(PendingIntent.getActivity(context, REQUEST_CODE_PENDING_INTENT, Intent(context, MainActivity::class.java), REQUEST_CODE_PENDING_INTENT))
        setCategory(NotificationCompat.CATEGORY_MESSAGE)
        setGroupSummary(true)

    }

    fun setMessageCount(messageCount: Int, threadCount: Int) {
        setSubText(context.getString(R.string.MessageNotifier_d_new_messages_in_d_conversations,
                messageCount, threadCount))
        setContentInfo(messageCount.toString())
        setNumber(messageCount)
    }


    fun addMessageBody(body: CharSequence?) {
        messageBodies.add(getStyledMessage(body))
    }

    override fun build(): Notification {
        val style = NotificationCompat.InboxStyle()

        for (body in messageBodies) {
            style.addLine(body)
        }
        setStyle(style)
        return super.build()
    }
}
