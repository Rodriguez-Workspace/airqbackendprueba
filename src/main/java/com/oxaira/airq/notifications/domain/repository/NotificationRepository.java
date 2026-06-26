package com.oxaira.airq.notifications.domain.repository;

import com.oxaira.airq.notifications.domain.model.Notification;

public interface NotificationRepository {

    Notification generateNotification();

}