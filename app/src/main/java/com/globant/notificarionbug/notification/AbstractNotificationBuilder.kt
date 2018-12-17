package com.globant.notificarionbug.notification

import android.content.Context
import android.support.v4.app.NotificationCompat
import android.text.SpannableStringBuilder

abstract class AbstractNotificationBuilder(protected var context: Context, channelId: String) : NotificationCompat.Builder(context, channelId) {

    protected fun getStyledMessage(message: CharSequence?): CharSequence {
        val builder = SpannableStringBuilder()
        builder.append(message ?: "")

        return builder
    }
}
