package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SupplementProfitInfo {
    private Date startDate;

    private Date endDate;

    private Integer standardOrderCount;

    private Integer comparisonType;

    private Date updatedTime;

    private List<String> storeIds;

    public SupplementProfitInfo(Date startDate, Date endDate, Integer standardOrderCount, Integer comparisonType, Date updatedTime, List<String> storeIds) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.standardOrderCount = standardOrderCount;
        this.comparisonType = comparisonType;
        this.updatedTime = updatedTime;
        this.storeIds = storeIds;
    }
}
