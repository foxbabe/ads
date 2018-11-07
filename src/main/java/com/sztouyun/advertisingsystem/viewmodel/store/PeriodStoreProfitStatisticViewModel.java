package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeriodStoreProfitStatisticViewModel {
    private List<String> storeIds;
    private String advertisementId;
}
