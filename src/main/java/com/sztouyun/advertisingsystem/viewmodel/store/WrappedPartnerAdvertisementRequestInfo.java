package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;

/**
 * Created by szty on 2018/7/26.
 */
@Data
public class WrappedPartnerAdvertisementRequestInfo {
    private String id;
    private PartnerAdvertisementRequestInfo value;
}
