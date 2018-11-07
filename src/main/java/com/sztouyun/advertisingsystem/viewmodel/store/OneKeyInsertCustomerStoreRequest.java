package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;

/**
 * Created by szty on 2018/5/18.
 */
@Data
public class OneKeyInsertCustomerStoreRequest extends CustomerStoreInfoQueryRequest {
    private Integer insertCount;
}
