package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;

@Data
public class StoreInfoBaseCondition {
    private Integer storeType;

    private String storeName;

    private String deviceId;

    private String shopId;

    private Boolean available;

    private Integer storeSource;

    private Boolean isQualified;
}
