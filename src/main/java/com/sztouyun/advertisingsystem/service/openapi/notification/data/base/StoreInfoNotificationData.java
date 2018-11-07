package com.sztouyun.advertisingsystem.service.openapi.notification.data.base;

import java.util.List;

public abstract class StoreInfoNotificationData extends OpenApiNotificationData{

    private List<String> storeIds;

    public StoreInfoNotificationData(NotificationTypeEnum notificationType) {
        super(notificationType);
    }

    public List<String> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<String> storeIds) {
        this.storeIds = storeIds;
    }
}
