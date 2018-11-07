package com.sztouyun.advertisingsystem.model.monitor;

import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 合作方每日门店监控统计
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "index_date_store_position_partner"
                ,columnList = "date,store_id,advertisement_position_category,partner_id",unique = true)
})
public class PartnerDailyStoreMonitorStatistic{
    public PartnerDailyStoreMonitorStatistic() {
    }

    public PartnerDailyStoreMonitorStatistic(String partnerId, Date date, String storeId, Integer advertisementPositionCategory) {
        this.partnerId = partnerId;
        this.date = date;
        this.storeId = storeId;
        this.advertisementPositionCategory = advertisementPositionCategory;
    }

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "partner_id",updatable = false,length = 36)
    private String partnerId;

    /**
     * 监控日期
     */
    private Date date;

    /**
     * 门店ID
     */
    @Column(name = "store_id",updatable = false,length = 36)
    private String storeId;

    /**
     * 广告位置: {@link AdvertisementPositionCategoryEnum}
     */
    @Column(name = "advertisement_position_category")
    private Integer advertisementPositionCategory = AdvertisementPositionCategoryEnum.FullScreen.getValue();

    /**
     * 成功获取广告次数
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer getAdTimes = 0;

    /**
     * 未获取广告次数
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer getNoAdTimes = 0;

    /**
     * 接口异常次数
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer apiErrorTimes = 0;

    /**
     * 展示次数
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer displayTimes = 0;

    /**
     * 有效展示次数
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer validTimes = 0;

    /**
     * 收益金额(单位:分)
     */
    @Column(nullable = false,columnDefinition = "bigint(20) default 0")
    private Long profitAmount = 0L;
}
