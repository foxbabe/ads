package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by szty on 2018/5/10.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerAdvertisementStoreDeliveryLogStatistic {
    private String partnerAdvertisementStoreId;
    private Date createdTime;
    private Long datetime=0L;
    private Integer displayTimes=0;
}
