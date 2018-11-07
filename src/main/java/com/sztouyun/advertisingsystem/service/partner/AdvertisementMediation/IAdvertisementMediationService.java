package com.sztouyun.advertisingsystem.service.partner.AdvertisementMediation;

import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.viewmodel.partner.store.StoreInfoRequest;

import java.util.List;

public interface IAdvertisementMediationService{
    List<PartnerAdvertisementDeliveryRecord> getPartnerAdvertisements(StoreInfoRequest storeInfoRequest);
}
