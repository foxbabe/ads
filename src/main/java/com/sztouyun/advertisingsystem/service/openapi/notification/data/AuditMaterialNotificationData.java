package com.sztouyun.advertisingsystem.service.openapi.notification.data;


import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.NotificationTypeEnum;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.OpenApiCallBackNotificationData;

public class AuditMaterialNotificationData extends OpenApiCallBackNotificationData {

    public AuditMaterialNotificationData() {
        super(NotificationTypeEnum.AuditingMaterialNotification);
    }
}
