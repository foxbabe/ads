package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreInfoByAdvertisementIdInfo extends BasePageInfo {
    private String advertisementId;

    public StoreInfoByAdvertisementIdInfo(String advertisementId) {
        this.advertisementId = advertisementId;
    }
}
