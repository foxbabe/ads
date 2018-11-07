package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.viewmodel.common.MyPageOffsetRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CustomerStoreInfoQueryRequest extends MyPageOffsetRequest{

    private String customerStorePlanId;

    private String storeName;

    private Integer storeType;

    private String deviceId;

    private String shopId;

    private Boolean available;

    private Boolean hasAbnormalNode;

    private Boolean hasTestNode=Boolean.FALSE;

    private List<String> regionIds = new ArrayList<>();

    private StoreDataMapInfo storeDataMapInfo;

    private String provinceId;

    private String cityId;

    private String regionId;

    private Integer storeSource;

    private Boolean isWithCoordinate = Boolean.FALSE;

    private Date previousDate;

    private Boolean isCheck;

    private Boolean isQualified;
}
