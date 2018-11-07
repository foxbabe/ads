package com.sztouyun.advertisingsystem.model.adProfitShare;

import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 门店月收益流水
 * @author wenfeng
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "index_store_id",columnList = "store_id"),
        @Index(name = "index_settled_id",columnList = "settled_store_profit_id")
})
@NoArgsConstructor
public class PeriodStoreProfitStatistic{
    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    /**
     * 门店ID
     */
    @Column(name = "store_id",nullable = false,updatable = false,length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;

    /**
     * 分成金额
     */
    @Column(nullable = false)
    private Double shareAmount = 0D;

    /**
     * 是否结算
     * @return
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private Boolean settled = Boolean.FALSE;

    /**
     * 分成月份
     */
    @Column(nullable = false,name = "settled_month")
    private Date settledMonth;

    /**
     * 结算表ID
     */
    @Column(name = "settled_store_profit_id",nullable = false,updatable = true,length = 36)
    private String settledStoreProfitId="";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settled_store_profit_id",insertable = false,updatable = false)
    private SettledStoreProfit settledStoreProfit;

    public PeriodStoreProfitStatistic(String storeId, Double shareAmount, Date settledMonth) {
        this.storeId = storeId;
        this.shareAmount = shareAmount;
        this.settledMonth = settledMonth;
    }
}
