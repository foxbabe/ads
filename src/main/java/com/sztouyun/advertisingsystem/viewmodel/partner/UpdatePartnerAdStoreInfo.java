package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePartnerAdStoreInfo {
    private String partnerAdvertisementId;

    private String partnerAdvertisementStoreId;

    private String partnerMaterialId;

    public UpdatePartnerAdStoreInfo(String partnerAdvertisementId, String partnerAdvertisementStoreId, String partnerMaterialId) {
        this.partnerAdvertisementId = partnerAdvertisementId;
        this.partnerAdvertisementStoreId = partnerAdvertisementStoreId;
        this.partnerMaterialId = partnerMaterialId;
    }
}
