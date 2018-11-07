package com.sztouyun.advertisingsystem.service.partner.advertismentSource;

import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.viewmodel.partner.store.StoreInfoRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface IPartnerAdvertisementService {
    List<PartnerAdvertisementDeliveryRecord> getPartnerAdvertisements(StoreInfoRequest storeInfoRequest);
}