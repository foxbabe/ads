package com.sztouyun.advertisingsystem.model.mongodb.profit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告门店每日收益
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdvertisementStoreDailyProfit {

    public AdvertisementStoreDailyProfit(String advertisementId, String storeId, Long date) {
        this.advertisementId = advertisementId;
        this.storeId = storeId;
        this.date = date;
    }

    private  String id;

    /**
     * 广告ID
     */
    private String advertisementId;

    /**
     * 门店ID
     */
    private String storeId;

    /**
     * 日期（单位：ms）
     */
    private Long date;

    /**
     * 分成金额(单位:分)
     */
    private long shareAmount = 0;

    /**
     * 门店开机时长（单位：ms）
     */
    private Long bootTime ;

    /**
     * 门店开机时长标准值（单位：ms）
     */
    private Long bootTimeStandard ;

    /**
     * 门店订单数量
     */
    private int orderCount;

    /**
     * 广告是否激活
     */
    private boolean active;

    /**
     * 是否达标
     */
    private boolean isQualified;

    public void  calcIsQualified(){
        setQualified(isActive() && bootTime>=bootTimeStandard);
    }

    /**
     * 临时结算ID
     */
    private String tempSettlementId ="";

    /**
     * 结算ID
     */
    private String settlementId ="";

    /**
     * 是否结算
     */
    private boolean settled;
}
