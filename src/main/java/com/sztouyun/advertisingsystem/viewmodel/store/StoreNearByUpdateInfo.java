package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/8/14.
 */
@Data
public class StoreNearByUpdateInfo {
    private String id;
    private double longitude;
    private double latitude;
    private Date updatedTime;
}
