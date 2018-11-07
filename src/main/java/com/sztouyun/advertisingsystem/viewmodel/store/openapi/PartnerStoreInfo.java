package com.sztouyun.advertisingsystem.viewmodel.store.openapi;

import lombok.Data;

/**
 * Created by wenfeng on 2018/2/2.
 */
@Data
public class PartnerStoreInfo {
    /**
     * 门店编号
     */
    private String id;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 省份
     */

    private String province;
    /**
     * 城市
     */

    private String city;
    /**
     * 区域
     */

    private String region;

    /**
     * 门店详细地址
     */

    private String address;

}
