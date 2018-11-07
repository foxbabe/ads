package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.model.BaseModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by szty on 2018/8/22.
 */
@Entity
@Table(indexes = {
        @Index(name = "index_advertisement_id",columnList = "advertisement_id")})
@Data
public class AdvertisementSettlement extends BaseModel {
    /**
     * 广告ID
     */
    @Column(name = "advertisement_id",nullable = false, length = 36)
    private String advertisementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id",insertable = false,updatable = false)
    private Advertisement advertisement;

    @Column
    private Integer storeCount;

    /**
     * 单位分
     */
    @Column(nullable = false,columnDefinition = "bigint(20) default 0")
    private Long shareAmount=0L;

    @Column
    private boolean settled;

    /**
     * 分成模式 @{@link com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitModeEnum}
     */
    @Column
    private Integer advertisementProfitMode;

    @Column
    private Date settledTime;

    @Column
    private String settledUser;
}
