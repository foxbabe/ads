package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;

import java.util.Date;

@Data
public class StoreDailyUsage {
    private Date date;
    private String storeId;
}
