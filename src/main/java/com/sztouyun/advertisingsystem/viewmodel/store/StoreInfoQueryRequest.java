package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.viewmodel.common.MyPageOffsetRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class StoreInfoQueryRequest extends MyPageOffsetRequest{

    private String contractId;

    private Integer storeType;

    private String storeName;

    private String deviceId;

    private String shopId;

    private Boolean available;

    private Boolean hasAbnormalNode;

    private Boolean hasTestNode;

    private List<String> regionIds = new ArrayList<>();

    private Integer storeSource;

    private Boolean hasHeart;

    private Date heartStartTime;

    private Date heartEndTime;

    private StoreDataMapInfo storeDataMapInfo;

    private PaveGoodsConditionInfo paveGoodsConditionInfo = new PaveGoodsConditionInfo();

    private String provinceId;

    private String cityId;

    private String regionId;

    private Boolean isWithCoordinate = Boolean.FALSE;

    private String customerStorePlanId;

    private Boolean isCheck;

    private Boolean isQualified;
}
