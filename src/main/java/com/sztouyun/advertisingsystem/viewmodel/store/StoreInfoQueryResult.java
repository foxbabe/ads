package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;

@Data
public class StoreInfoQueryResult {

    private String id;

    private String storeName;

    private String provinceId;

    private String regionId;

    private String cityId;

    private Integer isChoose;

    private String deviceId;

    private Boolean available;

    private String shopId;

    private Boolean hasHeart;

    private String storeAddress;

    private Boolean isPaveGoods;

    private Boolean isQualified;
}
