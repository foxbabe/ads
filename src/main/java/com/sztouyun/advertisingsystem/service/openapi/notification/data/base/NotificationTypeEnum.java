package com.sztouyun.advertisingsystem.service.openapi.notification.data.base;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.*;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;

/**
 * Created by RiberLi on 2018/1/21 0021.
 */
public enum NotificationTypeEnum implements EnumMessage<Integer> {

    AuditingMaterialNotification(1,"物料审核结果通知", AuditMaterialNotificationData.class),
    PublishOrderNotification(2,"上刊结果通知", PublishOrderNotificationData.class),
    UploadDeliveryLogNotification(3,"上传投放日志", UploadDeliveryLogNotificationData.class),
    UpdateStoreInfoNotification(4,"更新门店信息通知", UpdateStoreInfoNotificationData.class),
    DeleteStoreInfoNotification(5,"删除门店信息通知", DeleteStoreInfoNotificationData.class)
    ;

    private Integer value;
    private String displayName;
    private Class<? extends OpenApiNotificationData> notificationDataClass;

    NotificationTypeEnum(Integer value, String displayName,Class<? extends OpenApiNotificationData> notificationDataClass) {
        this.value = value;
        this.displayName = displayName;
        this.notificationDataClass = notificationDataClass;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Class<? extends OpenApiNotificationData> getNotificationDataClass() {
        return notificationDataClass;
    }

    public OpenApiNotificationData getNotificationData(String json){
       return  ObjectMapperUtils.jsonToObject(json,getNotificationDataClass());
    }
}
