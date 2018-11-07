package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationEnum;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ValidateAdvertisementStatusOperation implements IActionOperation<AdvertisementOperationLog> {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    private Map<AdvertisementStatusEnum,AdvertisementOperationEnum> advertisementStatusMapping = new HashMap<AdvertisementStatusEnum, AdvertisementOperationEnum>() {
        {
            put(AdvertisementStatusEnum.PendingCommit, AdvertisementOperationEnum.Submit);
            put(AdvertisementStatusEnum.PendingAuditing, AdvertisementOperationEnum.Auditing);
            put(AdvertisementStatusEnum.PendingDelivery, AdvertisementOperationEnum.Delivery);
            put(AdvertisementStatusEnum.Delivering, AdvertisementOperationEnum.Finish);
        }
    };

    @Override
    public void operateAction(AdvertisementOperationLog advertisementOperationLog) {
        Advertisement advertisement = advertisementOperationLog.getAdvertisement();
        if(advertisement ==null){
            advertisement = advertisementRepository.findOne(advertisementOperationLog.getAdvertisementId());
        }
        AdvertisementOperationEnum targetOperationEnum = advertisementStatusMapping.get(advertisement.getAdvertisementStatusEnum());
        if (!advertisementOperationLog.getAdvertisementOperationEnum().equals(targetOperationEnum))
            throw new BusinessException("当前广告状态不支持本操作！");
    }
}
