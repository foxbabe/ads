package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by szty on 2018/6/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerAdvertisementStoreDeliveryLog implements Serializable,Comparable<PartnerAdvertisementStoreDeliveryLog> {
    private String partnerAdvertisementStoreId;
    private String storeId;
    private String partnerAdvertisementId;
    private Date createdTime;
    private Long datetime=0L;
    public PartnerAdvertisementStoreDeliveryLog(String partnerAdvertisementStoreId) {
        this.partnerAdvertisementStoreId = partnerAdvertisementStoreId;
    }

    @Override
    public int compareTo(PartnerAdvertisementStoreDeliveryLog obj) {
        return (int)(this.getCreatedTime().getTime()-obj.getCreatedTime().getTime());
    }
}
