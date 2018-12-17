package com.globant.notificarionbug.contact

import android.os.Parcel
import android.os.Parcelable

class ChatContact : Parcelable {
    val ownerJabberId: String
    val contactJabberId: String
    var name: String = ""
    var subscribeStatus: String = "subscribe_from"
    var lastMessage: String = ""
    var messageStatus: String = ""
    var online: Boolean = false
    var hasNewMessages: Boolean = false
    var countUnreadMessages: Int = 0
    var lastMessageTimestamp: Long = 0

    fun hasConversation(): Boolean = lastMessage != ""
    val initials: String by lazy { initInitials() }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readLong())

    constructor(ownerJabberId: String, jabberId: String, lastMessage: String, messageStatus: String, lastMessageTimestamp: Long,
                hasNewMessages: Boolean) {
        this.ownerJabberId = ownerJabberId
        this.contactJabberId = jabberId
        this.lastMessage = lastMessage
        this.messageStatus = messageStatus
        this.lastMessageTimestamp = lastMessageTimestamp
        this.hasNewMessages = hasNewMessages
    }

    constructor(ownerJabberId: String,
                jabberId: String,
                name: String,
                subscribeStatus: String = "subscribe_from",
                lastMessage: String = "",
                messageStatus: String = "",
                online: Boolean = false,
                countUnreadMessages: Int = 0,
                lastMessageTimestamp: Long = 0,
                hasNewMessages: Boolean = false
    ) {
        this.ownerJabberId = ownerJabberId
        this.contactJabberId = jabberId
        this.name = name
        this.subscribeStatus = subscribeStatus
        this.lastMessage = lastMessage
        this.messageStatus = messageStatus
        this.online = online
        this.countUnreadMessages = countUnreadMessages
        this.lastMessageTimestamp = lastMessageTimestamp
        this.hasNewMessages = hasNewMessages
    }

    private fun initInitials(): String {
        val FIRST_NAME_LETTER = 0
        val SECOND_NAME_LETTER = 1
        val MIN_SIZE_TO_GET_INITIALS = 1
        val MIN_LENGTH_FIRST_NAME_LETTER = 0
        val ONE_ELEMENT_SIZE = 1

        if (name == "") return ""
        val splits = name.split(" ")
        if (splits.size > MIN_SIZE_TO_GET_INITIALS) {
            var firstNameInitial = ""
            var lastNameInitial = ""
            if (splits[FIRST_NAME_LETTER].length > MIN_LENGTH_FIRST_NAME_LETTER)
                firstNameInitial = splits[FIRST_NAME_LETTER].substring(FIRST_NAME_LETTER, SECOND_NAME_LETTER)
            if (splits[splits.size - ONE_ELEMENT_SIZE].length > MIN_LENGTH_FIRST_NAME_LETTER)
                lastNameInitial = splits[splits.size - ONE_ELEMENT_SIZE].substring(FIRST_NAME_LETTER, SECOND_NAME_LETTER)
            return firstNameInitial + lastNameInitial
        }
        if (splits.isNotEmpty()) {
            return splits[FIRST_NAME_LETTER].substring(FIRST_NAME_LETTER, SECOND_NAME_LETTER)
        }
        return ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ownerJabberId)
        parcel.writeString(contactJabberId)
        parcel.writeString(name)
        parcel.writeString(subscribeStatus)
        parcel.writeString(lastMessage)
        parcel.writeString(messageStatus)
        parcel.writeByte(if (online) 1 else 0)
        parcel.writeInt(countUnreadMessages)
        parcel.writeLong(lastMessageTimestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatContact> {
        override fun createFromParcel(parcel: Parcel): ChatContact {
            return ChatContact(parcel)
        }

        override fun newArray(size: Int): Array<ChatContact?> {
            return arrayOfNulls(size)
        }
    }
}
