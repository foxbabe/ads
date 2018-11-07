package com.sztouyun.advertisingsystem.service.openapi.notification.data;


import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.NotificationTypeEnum;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.StoreInfoNotificationData;
import com.sztouyun.advertisingsystem.viewmodel.store.openapi.PartnerStoreInfo;
import lombok.Data;

import java.util.List;

@Data
public class UpdateStoreInfoNotificationData extends StoreInfoNotificationData {
    private List<PartnerStoreInfo> storeInfoList;
    public UpdateStoreInfoNotificationData() {
        super(NotificationTypeEnum.UpdateStoreInfoNotification);
    }
}
