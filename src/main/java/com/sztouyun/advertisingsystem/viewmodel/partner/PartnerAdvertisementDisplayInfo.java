package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.Data;

@Data
public class PartnerAdvertisementDisplayInfo {
    public PartnerAdvertisementDisplayInfo() {
    }

    public PartnerAdvertisementDisplayInfo(String partnerAdvertisementId, Long totalDisplayTimes) {
        this.partnerAdvertisementId = partnerAdvertisementId;
        this.totalDisplayTimes = totalDisplayTimes;
    }

    private String partnerAdvertisementId;
    private Long totalDisplayTimes = 0L;
    private Long validDisplayTimes = 0L;
}
