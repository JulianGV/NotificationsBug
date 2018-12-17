package com.globant.notificarionbug

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.globant.notificarionbug.contact.ChatContact
import com.globant.notificarionbug.contact.ContactMessage
import com.globant.notificarionbug.notification.ChatMessageNotification
import com.globant.notificarionbug.notification.NotificationChannels
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var countUser1 = 0
    var countUser2 = 0

    var userMessages1 =  ArrayList<String>()
    var contactMessages1 = ArrayList<ContactMessage>()

    var userMessages2 =  ArrayList<String>()
    var contactMessages2 = ArrayList<ContactMessage>()

    companion object {
        val EXTRA_USER_DATA_FROM_NOTIFICATION = "EXTRA_USER_DATA"
        val CONTACT_FROM_NOTIFICATION = "CONTACT_FROM_NOTIFICATION"
        val BACKGROUND_NOTIFICATION_FLOW = "BACKGROUND_NOTIFICATION_FLOW"

        fun getIntentContactListNotificationFlow(context: Context, contact: ChatContact, fromNotification: Boolean): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_USER_DATA_FROM_NOTIFICATION, contact as Parcelable)
            intent.putExtra(CONTACT_FROM_NOTIFICATION, fromNotification)
            intent.putExtra(BACKGROUND_NOTIFICATION_FLOW, true)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NotificationChannels.create(this)
        buttonUser1.setOnClickListener {

            val chatContact = ChatContact("1111", "1234567","Jhon Doe", "both","Message $countUser1",
                    "sended",true, 0, 0L, true)
            userMessages1.add(chatContact.lastMessage)
            countUser1++

            val contactMessage = ContactMessage(1, "1234567","Jhon Doe", userMessages1)
            contactMessages1.add(contactMessage)
            val contactInfo = contactMessages1.find { itMessages -> itMessages.ownerId == chatContact.contactJabberId }

            ChatMessageNotification.updateNotification(this,chatContact, contactInfo!!)
        }

        buttonUser2.setOnClickListener {

            val chatContact = ChatContact("2222", "89101112","Amanda Doe", "both","Message $countUser2",
                    "sended",true, 0, 0L, true)
            userMessages2.add(chatContact.lastMessage)
            countUser2++

            val contactMessage = ContactMessage(2, "89101112","Amanda Doe", userMessages2)
            contactMessages2.add(contactMessage)
            val contactInfo = contactMessages2.find { itMessages -> itMessages.ownerId == chatContact.contactJabberId }

            ChatMessageNotification.updateNotification(this,chatContact, contactInfo!!)
        }
    }
}
