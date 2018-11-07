package com.sztouyun.advertisingsystem.model.monitor;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_advertisement_id_date",columnList = "advertisement_id,date", unique = true)
        })
public class AdvertisingDailyStoreMonitorStatistic {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    /**
     * 每日时间
     */
    private Date date;

    /**
     * 广告ID
     */
    @Column(name = "advertisement_id",nullable = false, length = 36)
    private String advertisementId;

    /**
     * 已激活门店数
     *
     */
    private Integer activeStoreCount;
}
