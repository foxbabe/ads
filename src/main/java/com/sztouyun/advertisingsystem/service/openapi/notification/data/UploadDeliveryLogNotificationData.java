package com.sztouyun.advertisingsystem.service.openapi.notification.data;


import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.NotificationTypeEnum;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.OpenApiNotificationData;

public class UploadDeliveryLogNotificationData extends OpenApiNotificationData {

    public UploadDeliveryLogNotificationData() {
        super(NotificationTypeEnum.UploadDeliveryLogNotification);
    }
}
