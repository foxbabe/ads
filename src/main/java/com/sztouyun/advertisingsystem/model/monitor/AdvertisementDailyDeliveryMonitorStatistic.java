package com.sztouyun.advertisingsystem.model.monitor;

import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_advertisement_id_date",columnList = "advertisement_id,date")
})
public class AdvertisementDailyDeliveryMonitorStatistic extends BaseAutoKeyEntity {

    /**
     * 监控日期
     */
    private Date date;

    /**
     * 广告位置
     */
    private Integer advertisementPositionCategory;

    /**
     * 展示次数
     */
    private Integer displayTimes;

    /**
     * 门店ID
     */
    @Column(name = "store_id",updatable = false,length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;

    /**
     * 广告ID
     */
    @Column(name = "advertisement_id",nullable = false, length = 36)
    private String advertisementId;
}
