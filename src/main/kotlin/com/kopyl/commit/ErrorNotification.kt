package com.kopyl.commit

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

object ErrorNotification {
    fun show(errorMessage: String) {
        val notification = Notification("Error Notifications", "Auto commit message", errorMessage, NotificationType.ERROR)
        Notifications.Bus.notify(notification)
    }
}
