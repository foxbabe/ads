package com.sztouyun.advertisingsystem.viewmodel.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdvertisementDisplayTimesViewModel {
    private String advertisementId;

    private Long displayTimes;
}
