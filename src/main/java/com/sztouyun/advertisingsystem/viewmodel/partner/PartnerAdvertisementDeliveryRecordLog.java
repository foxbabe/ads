package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartnerAdvertisementDeliveryRecordLog {
    private String recordId;
    private Long createdTime;
    private Integer action;
}
