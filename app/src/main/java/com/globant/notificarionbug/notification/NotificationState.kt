package com.globant.notificarionbug.notification

import java.util.LinkedHashSet
import java.util.LinkedList

class NotificationState {

    private var notifications = LinkedList<NotificationItem>()
    var threads = LinkedHashSet<Int>()
    private val EMPTY_ARRAY = 0
    private val MIN_THREAD_NUMBER = 1

    var messageCount = 0
        private set

    val threadCount: Int
        get() = threads.size

    constructor(){
        if(notifications.size > EMPTY_ARRAY)
           notifications.clear()
        if(threads.size > EMPTY_ARRAY)
            threads.clear()
    }

    constructor(items: List<NotificationItem>) {
        for (item in items) {
            addNotification(item)
        }
    }

    fun addNotification(item: NotificationItem) {
        notifications.addFirst(item)

        if (threads.contains(item.threadId)) {
            threads.remove(item.threadId)
        }

        threads.add(item.threadId)
        messageCount++
    }

    fun hasMultipleThreads(): Boolean {
        return threads.size > MIN_THREAD_NUMBER
    }

    fun getNotifications(): List<NotificationItem> {
        return notifications
    }

    fun getNotificationsForThread(threadId: Int): List<NotificationItem> {
        val list = LinkedList<NotificationItem>()

        for (item in notifications) {
            if (item.threadId == threadId) {
                list.addFirst(item)
            }
        }

        return list
    }
}
