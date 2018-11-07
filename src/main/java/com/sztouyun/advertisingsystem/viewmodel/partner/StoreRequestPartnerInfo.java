package com.sztouyun.advertisingsystem.viewmodel.partner;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by szty on 2018/9/3.
 */
@Data
public class StoreRequestPartnerInfo {
    private String shopId;
    private String storeName;
    private String provinceId;
    private String cityId;
    private String regionId;
    private String provinceName;
    private String cityName;
    private String regionName;
    private Long requestTimes=0L;
    private Long successfulTimes=0L;
    private Long validTimes=0L;

}
