package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;

import java.util.Date;

@Data
public class OneKeyInsertStoreInfoRequest extends StoreInfoQueryRequest {

    private Integer insertCount;

    private Date previousDate;
}
