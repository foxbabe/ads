package com.sztouyun.advertisingsystem.model.adProfitShare;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 门店收益 实体
 * @author wyf
 * @date 2018年1月10日11:58:14
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "index_store_profit_date",columnList = "store_id,profit_date"),
        @Index(name = "index_period_store_profit_statistic_id",columnList = "period_store_profit_statistic_id")
})
public class StoreProfitStatistic extends BaseModel {
    /**
     * 门店ID
     */
    @Column(name = "store_id",nullable = false,updatable = false,length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;

    /**
     * 月流水表ID
     */
    @Column(name ="period_store_profit_statistic_id",nullable = false,updatable = true,length = 36)
    private String periodStoreProfitStatisticId="";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_store_profit_statistic_id",insertable = false,updatable = false)
    private PeriodStoreProfitStatistic periodStoreProfitStatistic;

    /**
     * 日期
     */
    @Column(nullable = false,name = "profit_date")
    private Date profitDate;

    /**
     * 分成金额
     */
    @Column(nullable = false)
    private Double shareAmount = 0D;

    /**
     * 收益广告数量
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer effectiveAdvertisementCount = 0;

    /**
     * 门店下广告总数量
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer totalAdvertisementCount = 0;

    /**
     * 开机是否达标
     */
    @Column(nullable = false)
    private boolean openingTimeStandardIs;

    /**
     * 订单是否达标
     */
    @Column(nullable = false)
    private boolean orderStandardIs;

    /**
     * 门店是否达标
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private Boolean isQualified;

    /**
     * 扩展信息懒加载
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "storeProfitStatistic", fetch = FetchType.LAZY, optional = false)
    private StoreProfitStatisticExtension storeProfitStatisticExtension = new StoreProfitStatisticExtension();

    /**
     * 是否结算
     * @return
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private Boolean settled = Boolean.FALSE;
}
