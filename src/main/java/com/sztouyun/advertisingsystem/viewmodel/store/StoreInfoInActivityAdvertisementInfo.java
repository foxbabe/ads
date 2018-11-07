package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class StoreInfoInActivityAdvertisementInfo extends BasePageInfo {
    private String advertisementId;
    private Date date;

    public StoreInfoInActivityAdvertisementInfo(String advertisementId, Date date, int pageIndex, int pageSize) {
        this.advertisementId = advertisementId;
        this.date = date;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

}
