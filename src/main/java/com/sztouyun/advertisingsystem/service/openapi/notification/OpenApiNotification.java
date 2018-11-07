package com.sztouyun.advertisingsystem.service.openapi.notification;

import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.OpenApiNotificationData;
import org.springframework.context.ApplicationEvent;

/**
 * Created by RiberLi on 2018/1/21 0021.
 */
public class OpenApiNotification<TNotificationData extends OpenApiNotificationData> extends ApplicationEvent {
    private TNotificationData notificationData;

    public OpenApiNotification(TNotificationData notificationData) {
        super(notificationData);
        this.notificationData = notificationData;
    }

    public TNotificationData getNotificationData() {
        return notificationData;
    }
}
