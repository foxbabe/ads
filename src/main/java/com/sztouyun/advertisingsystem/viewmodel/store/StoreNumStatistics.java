package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class StoreNumStatistics {
    /**
     * 日期
     */
    private Date date;

    /**
     * 门店总数
     */
    private long storeTotal;

    /**
     * 满刊门店数量
     */
    private long fullStoreNum;

    /**
     * 已用广告位为0的门店数量
     */
    private long usedAdPositionEqualZeroStoreNum;

    /**
     * 已用广告位50%以下的门店数量
     */
    private long usedAdPositionLessThanFiftyPercentStoreNum;

    /**
     * 已用广告位50%以上的门店数量
     */
    private long usedAdPositionGreaterThanEqualFiftyPercentStoreNum;
}
