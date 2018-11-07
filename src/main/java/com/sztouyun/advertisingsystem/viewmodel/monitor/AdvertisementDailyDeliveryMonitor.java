package com.sztouyun.advertisingsystem.viewmodel.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDailyDeliveryMonitor {

    private Date endTime;

    List<DailyDeliveryMonitorStatistic> dailyDeliveryMonitorStatistics = new ArrayList<>();
}
