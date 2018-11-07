package com.sztouyun.advertisingsystem.viewmodel.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateAdvertisementStoreMonitor {

    private Date endTime;

    private List<DeliveryMonitorStatistic> deliveryMonitorStatistics = new ArrayList<>();

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<DeliveryMonitorStatistic> getDeliveryMonitorStatistics() {
        return deliveryMonitorStatistics;
    }

    public void setDeliveryMonitorStatistics(List<DeliveryMonitorStatistic> deliveryMonitorStatistics) {
        this.deliveryMonitorStatistics = deliveryMonitorStatistics;
    }

    public UpdateAdvertisementStoreMonitor(Date endTime, List<DeliveryMonitorStatistic> deliveryMonitorStatistics) {
        this.endTime = endTime;
        this.deliveryMonitorStatistics = deliveryMonitorStatistics;
    }
    public UpdateAdvertisementStoreMonitor() {}

}
