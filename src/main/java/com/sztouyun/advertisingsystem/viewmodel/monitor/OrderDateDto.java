package com.sztouyun.advertisingsystem.viewmodel.monitor;

import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/3/15.
 */
@Data
public class OrderDateDto {
    private String orderId;
    private Date date;

    public OrderDateDto(String orderId, Date date) {
        this.orderId = orderId;
        this.date = date;
    }
}
