package com.sztouyun.advertisingsystem.service.advertisement;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.system.AdvertisementDeliveryStrategy;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementDeliveryStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementDeliveryStrategyService {

    @Autowired
    private AdvertisementDeliveryStrategyRepository advertisementDeliveryStrategyRepository;

    public AdvertisementDeliveryStrategy getAdvertisementDeliveryStrategy() {
        return advertisementDeliveryStrategyRepository.findOne(new BooleanBuilder());
    }

    public String updateDeliveryStrategy(AdvertisementDeliveryStrategy advertisementDeliveryStrategy) {
        advertisementDeliveryStrategyRepository.save(advertisementDeliveryStrategy);
        return advertisementDeliveryStrategy.getId();
    }

}
