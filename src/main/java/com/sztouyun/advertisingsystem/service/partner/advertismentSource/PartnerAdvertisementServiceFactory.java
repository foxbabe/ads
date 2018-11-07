package com.sztouyun.advertisingsystem.service.partner.advertismentSource;


import org.springframework.stereotype.Service;

@Service
public class PartnerAdvertisementServiceFactory {
    public IPartnerAdvertisementService getPartnerAdvertisementService(String partnerId){
        AdvertisementSourceEnum advertisementSource =AdvertisementSourceEnum.getAdvertisementSource(partnerId);
        if(advertisementSource ==null)
            return null;
        return advertisementSource.getPartnerAdvertisementService();
    }
}
