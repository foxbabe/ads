package com.sztouyun.advertisingsystem.service.openapi.notification.data;


import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.NotificationTypeEnum;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.StoreInfoNotificationData;

public class DeleteStoreInfoNotificationData extends StoreInfoNotificationData {

    public DeleteStoreInfoNotificationData() {
        super(NotificationTypeEnum.DeleteStoreInfoNotification);
    }
}
