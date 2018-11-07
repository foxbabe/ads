package com.sztouyun.advertisingsystem.viewmodel.monitor;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class OrderDailyMonitorDto {
    private List<String> orderIds;
    /**
     * 每日时间
     */
    private Date date;

    public OrderDailyMonitorDto(List<String> orderIds, Date date) {
        this.orderIds = orderIds;
        this.date = date;
    }
}
