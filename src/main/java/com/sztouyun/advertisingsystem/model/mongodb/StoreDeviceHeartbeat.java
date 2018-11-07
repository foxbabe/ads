package com.sztouyun.advertisingsystem.model.mongodb;

import lombok.Data;

@Data
public class StoreDeviceHeartbeat {
    private String storeId;

    /**
     * 网络类型 ConnectionTypeEnum
     */
    private int connectionType;

    private Long createdTime;

    private Long createdDate;
}
