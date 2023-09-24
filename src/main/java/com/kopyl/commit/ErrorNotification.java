package com.kopyl.commit;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class ErrorNotification {
    public static void show(String errorMessage) {
        Notification notification = new Notification("Error Notifications", "Auto commit message", errorMessage, NotificationType.ERROR);
        Notifications.Bus.notify(notification);
    }
}
