package com.sztouyun.advertisingsystem.service.openapi.notification.data;


import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.NotificationTypeEnum;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.OpenApiCallBackNotificationData;

public class PublishOrderNotificationData extends OpenApiCallBackNotificationData {

    public PublishOrderNotificationData() {
        super(NotificationTypeEnum.PublishOrderNotification);
    }
}
