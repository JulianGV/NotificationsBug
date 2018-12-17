package com.globant.notificarionbug.notification

import android.app.Notification
import android.content.Context
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.text.SpannableStringBuilder
import com.globant.notificarionbug.R
import java.util.*

class SingleRecipientNotificationBuilder(context: Context, channelId: String) : AbstractNotificationBuilder(context, channelId) {

    private val messageBodies = LinkedList<CharSequence>()

    private var contentTitle: CharSequence? = null
    private var contentText: CharSequence? = null
    private val MIN_MESSAGES_NUMBER = 1

    init {

        setSmallIcon(R.mipmap.ic_launcher)
        setCategory(NotificationCompat.CATEGORY_MESSAGE)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        setSound(defaultSoundUri)
        if (!NotificationChannels.supported()) {
            priority = NotificationCompat.PRIORITY_HIGH
            setDefaults(NotificationCompat.DEFAULT_ALL)
        }

    }

    fun setThread(title: String) {
        setContentTitle(title)
    }

    fun setMessageCount(messageCount: Int) {
        setContentInfo(messageCount.toString())
        setNumber(messageCount)
    }

    fun setPrimaryMessageBody(message: CharSequence) {
        val stringBuilder = SpannableStringBuilder()
        setContentText(stringBuilder.append(message))

    }

    fun addMessageBody(messageBody: CharSequence?) {
        val stringBuilder = SpannableStringBuilder()
        messageBodies.add(stringBuilder.append(messageBody ?: ""))
    }

    override fun build(): Notification {
        if (messageBodies.size == MIN_MESSAGES_NUMBER ) {
            setStyle(NotificationCompat.BigPictureStyle()
                    .setSummaryText(getBigText(messageBodies)))
        } else {
            setStyle(NotificationCompat.BigTextStyle().bigText(getBigText(messageBodies)))
        }

        return super.build()
    }

    override fun setContentTitle(contentTitle: CharSequence): NotificationCompat.Builder {
        this.contentTitle = contentTitle
        return super.setContentTitle(contentTitle)
    }

    override fun setContentText(contentText: CharSequence): NotificationCompat.Builder {
        this.contentText = contentText
        return super.setContentText(contentText)
    }

    private fun getBigText(messageBodies: List<CharSequence>): CharSequence {
        val content = SpannableStringBuilder()

        for (i in messageBodies.indices) {
            content.append(messageBodies[i])
            if (i < messageBodies.size - MIN_MESSAGES_NUMBER) {
                content.append('\n')
            }
        }

        return content
    }
}
