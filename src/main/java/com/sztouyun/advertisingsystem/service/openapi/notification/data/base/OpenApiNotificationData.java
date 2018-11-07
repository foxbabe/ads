package com.sztouyun.advertisingsystem.service.openapi.notification.data.base;

/**
 * Created by RiberLi on 2018/1/21 0021.
 */
public abstract class OpenApiNotificationData{
    private NotificationTypeEnum notificationType;
    private String partnerId;

    public OpenApiNotificationData(NotificationTypeEnum notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationTypeEnum getNotificationType() {
        return notificationType;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }
}
