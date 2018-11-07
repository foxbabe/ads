package com.sztouyun.advertisingsystem.model.mongodb.profit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告门店每日收益
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDailyProfit {
    public StoreDailyProfit(String storeId,Long date) {
        this.storeId = storeId;
        this.date = date;
    }

    private String id;
    private String storeId;
    private Long date;
    private long shareAmount = 0;
}
