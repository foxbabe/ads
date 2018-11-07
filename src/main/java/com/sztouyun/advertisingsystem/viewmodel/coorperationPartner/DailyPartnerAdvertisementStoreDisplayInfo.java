package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/7/31.
 */
@Data
public class DailyPartnerAdvertisementStoreDisplayInfo {
    private Date date;
    private String storeId;
    private String partnerId;
    private Integer advertisementPositionCategory;
    private Long displayTimes;
    private Long validTimes;
    private Long requestDate;

    public Date getDate() {
        return new Date(requestDate);
    }
}
