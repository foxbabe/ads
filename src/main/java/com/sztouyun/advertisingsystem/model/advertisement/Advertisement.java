package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.adProfitShare.AdvertisementProfit;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitModeEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wenfeng on 2017/9/4.
 */
@Data
@Entity
public class Advertisement extends BaseModel {
    @Column(name = "contract_id",updatable = false,length = 36)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id",insertable = false,updatable = false)
    private Contract contract;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "advertisement", fetch = FetchType.LAZY, optional = false)
    private AdvertisementProfit advertisementProfit= new AdvertisementProfit();

    @Column(length = 255)
    private String data;

    /**
     * 客户ID
     */
    @Column(name = "customer_id",nullable = false,updatable = false,length = 36)
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    private Customer customer;

    @Column(nullable = false)
    private Integer advertisementType=1;

    @Column(nullable = false)
    private String advertisementName;

    /**
     * 广告预期开始投放时间
     */
    @Column(nullable = false)
    private Date startTime;

    /**
     * 广告预期结束投放时间
     */
    @Column(nullable = false)
    private Date endTime;

    @Column(length = 2000)
    private String remark;

    @Column(nullable = false,columnDefinition = "Integer default  1")
    private Integer advertisementStatus=1;

    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer advertisementPeriod=0;

    /**
     * 分成模式 @{@link AdvertisementProfitModeEnum}
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer mode = 0;

    /**
     * 分成金额
     */
    @Column(nullable = false,columnDefinition = "bigint(20) default  0")
    private Long shareAmount = 0L;

    /**
     * 已结算金额
     */
    @Column(nullable = false,columnDefinition = "bigint(20) default  0")
    private Long settledAmount = 0L;

    /**
     * 实际投放开始时间
     */
    @Column
    private Date effectiveStartTime;

    /**
     * 实际投放截止时间
     */
    @Column
    private Date effectiveEndTime;

    /**
     * 实际投放周期
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer effectivePeriod=0;

    /**
     * 投放后的预计截止时间
     */
    @Column
    private Date expectedDueDay;

    /**
     * 投放人
     */
    @Column(name = "delivery_operator_id",length = 36)
    private String deliveryOperatorId;

    /**
     * 是否开启分成
     */
    @Column(nullable = false)
    private boolean enableProfitShare;

    /**
     * 分成标准金额
     */
    private Double profitShareStandardAmount;

    /**
     * 分成标准金额单位
     */
    private Integer profitShareStandardAmountUnit;

    public AdvertisementStatusEnum  getAdvertisementStatusEnum(){
        return EnumUtils.toEnum(getAdvertisementStatus(),AdvertisementStatusEnum.class);
    }
}
