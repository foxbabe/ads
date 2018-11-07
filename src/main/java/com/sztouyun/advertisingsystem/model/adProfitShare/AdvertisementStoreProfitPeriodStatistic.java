package com.sztouyun.advertisingsystem.model.adProfitShare;

import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 广告门店分成月度统计表
 */
@Entity
@Data
@Table(indexes = {
        @Index(name = "index_advertisement_store",columnList = "advertisement_id,store_id")
})
public class AdvertisementStoreProfitPeriodStatistic extends BaseAutoKeyEntity {
    /**
     * 广告ID
     */
    @Column(name = "advertisement_id",nullable = false,updatable = false,length = 36)
    private String advertisementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id",insertable = false,updatable = false)
    private Advertisement advertisement;

    /**
     * 门店ID
     */
    @Column(name = "store_id",nullable = false,updatable = false,length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;

    /**
     * 分成月份
     */
    @Column(nullable = false,name = "settled_month")
    private Date settledMonth;

    /**
     * 分成金额
     */
    @Column(nullable = false)
    private Double shareAmount = 0D;

    /**
     * 已结算
     */
    @Column(nullable = false)
    private Double settledAmount = 0D;
}